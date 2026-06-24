package in.elcot.avgcxr.platform.notification.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "title_tamil", length = 300)
    private String titleTamil;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "body_tamil", columnDefinition = "TEXT")
    private String bodyTamil;

    @Column(name = "channel", length = 20)
    private String channel;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
