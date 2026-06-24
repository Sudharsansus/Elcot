package in.elcot.avgcxr.chat.api.rest.dto.response;

import in.elcot.avgcxr.chat.domain.model.ChatSession;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatSessionResponse(
        UUID id,
        String sessionToken,
        String title,
        String language,
        int messageCount,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant expiresAt,
        List<ChatMessageResponse> recentMessages
) {
    public static ChatSessionResponse from(ChatSession s, List<ChatMessageResponse> recent) {
        return new ChatSessionResponse(
                s.getId(),
                s.getSessionToken(),
                s.getTitle(),
                s.getLanguage(),
                s.getMessageCount(),
                s.isActive(),
                s.getCreatedAt(),
                s.getUpdatedAt(),
                s.getExpiresAt(),
                recent
        );
    }
}
