package in.elcot.avgcxr.chat.api.rest.dto.response;

import java.util.List;
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
    long totalLatencyMs) {}
