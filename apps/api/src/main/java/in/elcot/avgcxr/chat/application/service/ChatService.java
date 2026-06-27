package in.elcot.avgcxr.chat.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.elcot.avgcxr.chat.application.port.output.ChatMessageRepositoryPort;
import in.elcot.avgcxr.chat.application.port.output.ChatSessionRepositoryPort;
import in.elcot.avgcxr.chat.application.service.PromptBuilder.RagDocument;
import in.elcot.avgcxr.chat.application.service.llm.LlmProvider;
import in.elcot.avgcxr.chat.application.service.llm.LlmProvider.LlmResponse;
import in.elcot.avgcxr.chat.domain.model.ChatMessage;
import in.elcot.avgcxr.chat.domain.model.ChatRole;
import in.elcot.avgcxr.chat.domain.model.ChatSession;
import in.elcot.avgcxr.common.infrastructure.security.ChatSafetyGuard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Core chat service: orchestrates session, RAG, LLM, and persistence.
 *
 * <p>Flow per message:
 *
 * <ol>
 *   <li>Resolve/create session
 *   <li>Detect language (Tamil script → ta; otherwise honor session.lang)
 *   <li>Retrieve RAG context (ES + PG fallback)
 *   <li>Build bilingual system prompt + history
 *   <li>Call LLM provider
 *   <li>Persist user + assistant messages with RAG IDs and timing
 *   <li>Update session metadata
 * </ol>
 */
@Service
public class ChatService {

  private static final Logger log = LoggerFactory.getLogger(ChatService.class);
  private static final int MAX_HISTORY = 20; // last N turns in prompt
  private static final int MAX_HISTORY_TOKENS_EST = 8000;

  private static final ObjectMapper ACTION_MAPPER = new ObjectMapper();
  private static final Pattern ACTION_PATTERN =
      Pattern.compile("\\[\\[action:(\\{.*?})]]", Pattern.DOTALL);
  private static final Set<String> ALLOWED_TOOLS =
      Set.of("navigate", "openSchemeFinder", "fillForm", "findScheme", "listSchemes");

  private final LlmProvider llm;
  private final RagService ragService;
  private final ChatSessionRepositoryPort sessionRepo;
  private final ChatMessageRepositoryPort messageRepo;
  private final PromptBuilder promptBuilder;
  private final TamilSupport tamilSupport;

  public ChatService(
      LlmProvider llm,
      RagService ragService,
      ChatSessionRepositoryPort sessionRepo,
      ChatMessageRepositoryPort messageRepo,
      PromptBuilder promptBuilder,
      TamilSupport tamilSupport) {
    this.llm = llm;
    this.ragService = ragService;
    this.sessionRepo = sessionRepo;
    this.messageRepo = messageRepo;
    this.promptBuilder = promptBuilder;
    this.tamilSupport = tamilSupport;
  }

  /** Send a user message and get a complete reply. */
  @Transactional
  public ChatTurnResult sendMessage(
      UUID userId, String sessionToken, String userMessage, String preferredLanguage) {
    long start = System.currentTimeMillis();
    if (userMessage == null || userMessage.isBlank()) {
      throw new IllegalArgumentException("userMessage must not be blank");
    }
    // 1) Resolve or create session
    ChatSession session = resolveSession(userId, sessionToken, preferredLanguage);
    String language = tamilSupport.detectLanguage(userMessage, session.getLanguage());
    if (!language.equals(session.getLanguage())) {
      session.setLanguage(language);
    }

    // 2) Persist user message
    ChatMessage userMsg = ChatMessage.userMessage(session.getId(), userMessage, language);
    messageRepo.save(userMsg);
    session.incrementMessageCount();

    // 2b) Policy guard: this endpoint is public/unauthenticated, so refuse any attempt to pull
    // other people's, bulk, or administrative data before touching RAG or the LLM.
    boolean blocked = ChatSafetyGuard.looksLikeDataExfil(userMessage);

    // 3) RAG (skipped for blocked requests; retrieved context is PII-redacted in RagService)
    List<RagDocument> ragDocs = blocked ? List.of() : ragService.retrieve(userMessage, language);
    List<String> ragIds = ragDocs.stream().map(RagDocument::id).toList();

    // 4) Build prompt
    String systemPrompt = promptBuilder.buildSystemPrompt(language, ragDocs);
    List<ChatMessage> history = messageRepo.findRecentBySessionId(session.getId(), MAX_HISTORY);
    List<Map<String, String>> llmMessages = new ArrayList<>();
    for (ChatMessage m : history) {
      String role = m.getRole() == ChatRole.ASSISTANT ? "assistant" : "user";
      llmMessages.add(PromptBuilder.msg(role, m.getContent()));
    }

    // 5) Call LLM (or return the policy refusal directly)
    LlmResponse llmResp;
    if (blocked) {
      log.warn("Chat data-exfil attempt blocked (session {})", session.getId());
      llmResp =
          new LlmResponse(
              ChatSafetyGuard.refusalMessage(language),
              "policy-guard",
              0,
              0,
              0,
              System.currentTimeMillis() - start);
    } else {
      try {
        llmResp = llm.complete(systemPrompt, llmMessages, LlmProvider.LlmOptions.defaults());
      } catch (Exception e) {
        log.error("LLM call failed: {}", e.getMessage());
        // Fallback to rule-based if the configured provider is down
        llmResp =
            new LlmResponse(
                "I'm having trouble reaching the AI service right now. "
                    + "Please try again, or visit /helpdesk for human support.",
                "fallback",
                0,
                0,
                0,
                System.currentTimeMillis() - start);
      }
    }

    // 5b) Parse any agent action the model emitted; strip the tag from the visible reply
    ParsedReply parsed = extractAction(llmResp.content());

    // 6) Persist assistant message (the user-facing text, without the action tag)
    ChatMessage assistantMsg =
        ChatMessage.assistantMessage(
            session.getId(),
            parsed.text(),
            language,
            llmResp.model(),
            llmResp.totalTokens(),
            (int) llmResp.latencyMs(),
            ragIds);
    messageRepo.save(assistantMsg);
    session.incrementMessageCount();

    // 7) Auto-generate title from first user message
    if (session.getTitle() == null && !userMessage.isBlank()) {
      session.setTitle(
          userMessage.length() > 60 ? userMessage.substring(0, 60) + "..." : userMessage);
    }
    sessionRepo.save(session);

    return new ChatTurnResult(
        session,
        userMsg,
        assistantMsg,
        ragDocs,
        llmResp.model(),
        llmResp.totalTokens(),
        System.currentTimeMillis() - start,
        parsed.action());
  }

  /** The user-facing reply text plus an optional, validated agent action. */
  private record ParsedReply(String text, Map<String, Object> action) {}

  /**
   * Pull a trailing {@code [[action:{...}]]} tag (if any) out of the model output, validate it
   * against the allow-list, and return the cleaned reply + action. Anything malformed or unknown is
   * ignored so the chat degrades gracefully to plain text (and rule-based providers, which never
   * emit the tag, are unaffected).
   */
  private static ParsedReply extractAction(String content) {
    if (content == null) return new ParsedReply("", null);
    Matcher m = ACTION_PATTERN.matcher(content);
    if (!m.find()) return new ParsedReply(content.trim(), null);
    String clean = content.substring(0, m.start()).trim();
    String fallbackText = clean.isEmpty() ? content.trim() : clean;
    try {
      JsonNode node = ACTION_MAPPER.readTree(m.group(1));
      String tool = node.path("tool").asText("");
      if (!ALLOWED_TOOLS.contains(tool)) return new ParsedReply(fallbackText, null);

      Map<String, Object> args = new HashMap<>();
      JsonNode argsNode = node.path("args");
      if (argsNode.isObject()) {
        argsNode.fields().forEachRemaining(e -> args.put(e.getKey(), e.getValue().asText()));
      }
      // per-tool validation (only internal routes / known forms allowed)
      if ("navigate".equals(tool)
          && !String.valueOf(args.getOrDefault("route", "")).startsWith("/")) {
        return new ParsedReply(fallbackText, null);
      }
      if ("fillForm".equals(tool)) {
        String form = String.valueOf(args.getOrDefault("form", ""));
        if (!"register".equals(form) && !"contact".equals(form)) {
          return new ParsedReply(fallbackText, null);
        }
      }
      Map<String, Object> action = new HashMap<>();
      action.put("tool", tool);
      action.put("args", args);
      return new ParsedReply(fallbackText, action);
    } catch (Exception e) {
      return new ParsedReply(fallbackText, null);
    }
  }

  @Transactional
  public ChatSession resolveSession(UUID userId, String sessionToken, String languageHint) {
    if (sessionToken != null && !sessionToken.isBlank()) {
      return sessionRepo
          .findBySessionToken(sessionToken)
          .orElseGet(() -> createNewSession(userId, languageHint, sessionToken));
    }
    return createNewSession(userId, languageHint, null);
  }

  private ChatSession createNewSession(UUID userId, String languageHint, String existingToken) {
    String lang = (languageHint == null || languageHint.isBlank()) ? "en" : languageHint;
    ChatSession s =
        (userId != null) ? ChatSession.newForUser(userId, lang) : ChatSession.newAnonymous(lang);
    if (existingToken != null) s.setSessionToken(existingToken);
    return sessionRepo.save(s);
  }

  /**
   * DPDP data minimisation: purge chat sessions past their retention window. Runs daily (cron +
   * timezone configurable). Enabled by {@code @EnableScheduling} in ChatConfig.
   */
  @Scheduled(
      cron = "${avgcxr.llm.session.purge-cron:0 0 3 * * *}",
      zone = "${avgcxr.llm.session.purge-timezone:Asia/Kolkata}")
  @Transactional
  public int purgeExpired() {
    int purged = sessionRepo.deleteExpired();
    if (purged > 0) {
      log.info("Purged {} expired chat sessions (DPDP retention)", purged);
    }
    return purged;
  }

  /** Result of a single turn. */
  public record ChatTurnResult(
      ChatSession session,
      ChatMessage userMessage,
      ChatMessage assistantMessage,
      List<RagDocument> ragDocuments,
      String modelUsed,
      int tokensUsed,
      long totalLatencyMs,
      Map<String, Object> action) {}
}
