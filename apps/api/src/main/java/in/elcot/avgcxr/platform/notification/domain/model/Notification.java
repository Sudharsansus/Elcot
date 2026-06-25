package in.elcot.avgcxr.platform.notification.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Domain aggregate root for Multi-channel notification dispatch (SMS, Email) with template support
 * and delivery tracking. This class contains business logic and validation rules. It has NO
 * dependencies on Spring or infrastructure.
 */
/**
 * Pure domain model — no framework imports. Persistence is handled by
 * infrastructure.persistence.entity + mapper.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Notification extends AuditableEntity {

  private UUID id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static Notification create() {
    return Notification.builder()
        .id(UUID.randomUUID())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
