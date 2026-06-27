package in.elcot.avgcxr.chat.api.rest.dto.response;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ChatTurnResponse(
    UUID sessionId,
    String sessionToken,
    String language,
    ChatMessageResponse userMessage,
    ChatMessageResponse assistantMessage,
    List<String> ragSourceIds,
    String modelUsed,
    int tokensUsed,
    long totalLatencyMs,
    /** Optional agent action the client should execute (tool + args), or null. */
    Map<String, Object> action) {}
