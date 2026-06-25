package in.elcot.avgcxr.analytics.chatbot.application.service;

import in.elcot.avgcxr.platform.search.application.port.output.SearchRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Mira — the AVGC-XR Portal's bilingual (English + Tamil) AI assistant.
 *
 * <p>Audit fix: the chatbot now delegates to an {@link LlmService} bean (RAG-ready). The default
 * {@link RuleBasedLlmService} handles requests when no LLM is configured. A future OpenAI /
 * Anthropic / local-model bean can replace it without changes to this class.
 *
 * <p>For RAG, this service can query the search repository for relevant scheme documents and pass
 * them to the LLM as context.
 */
@Service
public class MiraChatbotService {

  private final LlmService llm;
  private final SearchRepositoryPort searchRepository;

  public MiraChatbotService(LlmService llm, SearchRepositoryPort searchRepository) {
    this.llm = llm;
    this.searchRepository = searchRepository;
  }

  /**
   * Answer a user message in the given language. RAG: retrieves the top relevant items from the
   * search repository (when available) and passes them to the LLM as context.
   */
  public Map<String, Object> answer(String userMessage, String preferredLanguage) {
    List<Map<String, Object>> context = retrieveContext(userMessage);
    return llm.generate(userMessage, context, preferredLanguage);
  }

  /** Suggested questions for the UI. Bilingual. */
  public List<Map<String, String>> suggestedQuestions() {
    return List.of(
        Map.of(
            "text_en", "What schemes are available?",
            "text_ta", "என்ன திட்டங்கள் உள்ளன?"),
        Map.of(
            "text_en", "How do I apply for a scheme?",
            "text_ta", "ஒரு திட்டத்திற்கு எப்படி விண்ணப்பிப்பது?"),
        Map.of(
            "text_en", "How do I check my application status?",
            "text_ta", "எனது விண்ணப்பத்தின் நிலையை எப்படி சோதிப்பது?"),
        Map.of(
            "text_en", "What documents are required?",
            "text_ta", "என்ன ஆவணங்கள் தேவை?"),
        Map.of(
            "text_en", "What are the eligibility criteria?",
            "text_ta", "தகுதி தேவைகள் என்ன?"),
        Map.of(
            "text_en", "How do I file a grievance?",
            "text_ta", "புகாரை எப்படி தாக்கல் செய்வது?"));
  }

  /**
   * RAG: query the search repository for items related to the user message. If search is
   * unavailable, returns an empty list (LLM will respond without context, which is fine for the
   * rule-based fallback).
   */
  private List<Map<String, Object>> retrieveContext(String userMessage) {
    if (searchRepository == null || userMessage == null || userMessage.isBlank()) {
      return List.of();
    }
    try {
      // SearchRepositoryPort has a generic search; use it if available
      // (capped to 3 docs for prompt-size sanity)
      var results = searchRepository.fullTextSearch(userMessage, 3);
      List<Map<String, Object>> context = new ArrayList<>();
      if (results != null) {
        results.forEach(
            r -> {
              Map<String, Object> ctx = new java.util.LinkedHashMap<>();
              ctx.put("id", r.getId());
              ctx.put("name", r.getName() != null ? r.getName() : "");
              ctx.put("description", r.getDescription() != null ? r.getDescription() : "");
              context.add(ctx);
            });
      }
      return context;
    } catch (Exception e) {
      // Search unavailable — proceed without RAG context
      return List.of();
    }
  }
}
