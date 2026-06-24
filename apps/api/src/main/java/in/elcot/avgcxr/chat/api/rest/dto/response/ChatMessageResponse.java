package in.elcot.avgcxr.chat.api.rest.dto.response;

import in.elcot.avgcxr.chat.domain.model.ChatMessage;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatMessageResponse(
        UUID id,
        String role,
        String content,
        String language,
        String modelUsed,
        Integer tokensUsed,
        Integer latencyMs,
        List<String> ragContextIds,
        Instant createdAt
) {
    public static ChatMessageResponse from(ChatMessage m) {
        return new ChatMessageResponse(
                m.getId(),
                m.getRole() == null ? null : m.getRole().name(),
                m.getContent(),
                m.getLanguage(),
                m.getModelUsed(),
                m.getTokensUsed(),
                m.getLatencyMs(),
                m.getRagContextIds(),
                m.getCreatedAt()
        );
    }
}
