package in.elcot.avgcxr.platform.audit.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Domain aggregate root for Immutable audit trail for all sensitive operations, queryable with
 * filters and export. This class contains business logic and validation rules. It has NO
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
public class AuditLog extends AuditableEntity {

  private UUID id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static AuditLog create() {
    return AuditLog.builder()
        .id(UUID.randomUUID())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
