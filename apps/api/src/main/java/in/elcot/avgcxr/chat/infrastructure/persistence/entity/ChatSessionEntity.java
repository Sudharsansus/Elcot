package in.elcot.avgcxr.chat.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatSessionEntity {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "session_token", length = 64, unique = true)
    private String sessionToken;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "language", length = 5)
    private String language;

    @Column(name = "message_count", nullable = false)
    private int messageCount;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
}
