package in.elcot.avgcxr.chat.api.rest;

import in.elcot.avgcxr.chat.api.rest.dto.request.SendMessageRequest;
import in.elcot.avgcxr.chat.api.rest.dto.response.ChatMessageResponse;
import in.elcot.avgcxr.chat.api.rest.dto.response.ChatSessionResponse;
import in.elcot.avgcxr.chat.api.rest.dto.response.ChatTurnResponse;
import in.elcot.avgcxr.chat.application.port.output.ChatMessageRepositoryPort;
import in.elcot.avgcxr.chat.application.port.output.ChatSessionRepositoryPort;
import in.elcot.avgcxr.chat.application.service.ChatService;
import in.elcot.avgcxr.chat.application.service.ChatService.ChatTurnResult;
import in.elcot.avgcxr.chat.domain.model.ChatSession;
import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI chat agent REST API.
 *
 * <p>Public endpoint -- works for anonymous users (sessionToken cookie) and authenticated users
 * (userId from JWT).
 *
 * <p>Supports both REST (single response) and SSE (streamed response). Bilingual: English and
 * Tamil.
 */
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

  private final ChatService chatService;
  private final ChatSessionRepositoryPort sessionRepo;
  private final ChatMessageRepositoryPort messageRepo;

  public ChatController(
      ChatService chatService,
      ChatSessionRepositoryPort sessionRepo,
      ChatMessageRepositoryPort messageRepo) {
    this.chatService = chatService;
    this.sessionRepo = sessionRepo;
    this.messageRepo = messageRepo;
  }

  /** Send a message, get a complete response (REST). */
  @PostMapping("/send")
  public ResponseEntity<ApiResponse<ChatTurnResponse>> send(
      @Valid @RequestBody SendMessageRequest req, Authentication auth) {
    UUID userId = (auth != null && auth.getName() != null) ? currentUserIdFromAuth(auth) : null;
    ChatTurnResult r =
        chatService.sendMessage(userId, req.sessionToken(), req.message(), req.language());
    return ResponseEntity.ok(ApiResponse.success(toTurnResponse(r)));
  }

  /** Send a message with Server-Sent Events streaming. */
  @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter stream(@Valid @RequestBody SendMessageRequest req, Authentication auth) {
    UUID userId = (auth != null && auth.getName() != null) ? currentUserIdFromAuth(auth) : null;
    SseEmitter emitter = new SseEmitter(60_000L);
    new Thread(
            () -> {
              try {
                ChatTurnResult r =
                    chatService.sendMessage(
                        userId, req.sessionToken(), req.message(), req.language());
                // Emit the full message as a single event
                emitter.send(SseEmitter.event().name("session").data(toTurnResponse(r)));
                emitter.complete();
              } catch (IOException e) {
                emitter.completeWithError(e);
              } catch (Exception e) {
                emitter.completeWithError(e);
              }
            },
            "chat-stream")
        .start();
    return emitter;
  }

  /** Get full session history. */
  @GetMapping("/sessions/{token}")
  public ResponseEntity<ApiResponse<ChatSessionResponse>> getSession(@PathVariable String token) {
    ChatSession s =
        sessionRepo
            .findBySessionToken(token)
            .orElseThrow(() -> new RuntimeException("Session not found"));
    List<ChatMessageResponse> msgs =
        messageRepo.findBySessionId(s.getId()).stream().map(ChatMessageResponse::from).toList();
    return ResponseEntity.ok(ApiResponse.success(ChatSessionResponse.from(s, msgs)));
  }

  /** List all sessions for the current authenticated user. */
  @GetMapping("/sessions")
  public ResponseEntity<ApiResponse<List<ChatSessionResponse>>> listSessions(Authentication auth) {
    if (auth == null || auth.getName() == null) {
      return ResponseEntity.ok(ApiResponse.success(List.of()));
    }
    UUID userId = currentUserIdFromAuth(auth);
    List<ChatSessionResponse> sessions =
        sessionRepo.findByUserId(userId).stream()
            .map(s -> ChatSessionResponse.from(s, List.of()))
            .toList();
    return ResponseEntity.ok(ApiResponse.success(sessions));
  }

  /** Delete a session and all its messages. */
  @DeleteMapping("/sessions/{token}")
  public ResponseEntity<ApiResponse<Void>> deleteSession(@PathVariable String token) {
    ChatSession s = sessionRepo.findBySessionToken(token).orElse(null);
    if (s != null) {
      messageRepo.deleteBySessionId(s.getId());
      // session row will be cascade-deleted by FK; but call also
    }
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  /** Health check for the chat agent. */
  @GetMapping("/health")
  public ResponseEntity<ApiResponse<java.util.Map<String, Object>>> health() {
    var s = chatService.resolveSession(null, null, "en");
    return ResponseEntity.ok(
        ApiResponse.success(
            java.util.Map.of("status", "up", "newSessionId", s.getId().toString())));
  }

  // ---- helpers ----

  private ChatTurnResponse toTurnResponse(ChatTurnResult r) {
    return new ChatTurnResponse(
        r.session().getId(),
        r.session().getSessionToken(),
        r.session().getLanguage(),
        ChatMessageResponse.from(r.userMessage()),
        ChatMessageResponse.from(r.assistantMessage()),
        r.ragDocuments().stream().map(d -> d.id()).toList(),
        r.modelUsed(),
        r.tokensUsed(),
        r.totalLatencyMs(),
        r.action());
  }

  private UUID currentUserIdFromAuth(Authentication auth) {
    // The auth principal name is the username; for now derive a stable UUID
    return UUID.nameUUIDFromBytes(auth.getName().getBytes());
  }
}
