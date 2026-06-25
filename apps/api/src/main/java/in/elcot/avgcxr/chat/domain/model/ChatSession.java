package in.elcot.avgcxr.chat.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Pure domain representation of a chat session (a thread of messages for one user, or anonymous via
 * session_token).
 *
 * <p>Mutable for incremental updates (title, language, messageCount). Hexagonal: no framework
 * imports.
 */
@Getter
@Setter
public class ChatSession {

  private UUID id;
  private UUID userId; // nullable for anonymous
  private String sessionToken;
  private String title;
  private String language; // 'en' or 'ta'
  private int messageCount;
  private boolean isActive;
  private Map<String, Object> metadata;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant expiresAt;

  public ChatSession() {}

  public static ChatSession newAnonymous(String language) {
    ChatSession s = new ChatSession();
    Instant now = Instant.now();
    s.id = UUID.randomUUID();
    s.sessionToken = UUID.randomUUID().toString().replace("-", "");
    s.language = language == null ? "en" : language;
    s.messageCount = 0;
    s.isActive = true;
    s.createdAt = now;
    s.updatedAt = now;
    s.expiresAt = now.plusSeconds(90 * 24 * 3600L);
    return s;
  }

  public static ChatSession newForUser(UUID userId, String language) {
    ChatSession s = newAnonymous(language);
    s.userId = userId;
    return s;
  }

  public void incrementMessageCount() {
    this.messageCount++;
    this.updatedAt = Instant.now();
  }
}
