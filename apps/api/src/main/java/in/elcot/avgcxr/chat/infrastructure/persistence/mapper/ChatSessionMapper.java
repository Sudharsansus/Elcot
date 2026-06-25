package in.elcot.avgcxr.chat.infrastructure.persistence.mapper;

import in.elcot.avgcxr.chat.domain.model.ChatSession;
import in.elcot.avgcxr.chat.infrastructure.persistence.entity.ChatSessionEntity;

public final class ChatSessionMapper {
  private ChatSessionMapper() {}

  public static ChatSession toDomain(ChatSessionEntity e) {
    if (e == null) return null;
    ChatSession s = new ChatSession();
    s.setId(e.getId());
    s.setUserId(e.getUserId());
    s.setSessionToken(e.getSessionToken());
    s.setTitle(e.getTitle());
    s.setLanguage(e.getLanguage());
    s.setMessageCount(e.getMessageCount());
    s.setActive(e.isActive());
    s.setCreatedAt(e.getCreatedAt());
    s.setUpdatedAt(e.getUpdatedAt());
    s.setExpiresAt(e.getExpiresAt());
    return s;
  }

  public static ChatSessionEntity toEntity(ChatSession s) {
    if (s == null) return null;
    return ChatSessionEntity.builder()
        .id(s.getId())
        .userId(s.getUserId())
        .sessionToken(s.getSessionToken())
        .title(s.getTitle())
        .language(s.getLanguage())
        .messageCount(s.getMessageCount())
        .isActive(s.isActive())
        .createdAt(s.getCreatedAt())
        .updatedAt(s.getUpdatedAt())
        .expiresAt(s.getExpiresAt())
        .build();
  }
}
