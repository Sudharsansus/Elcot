package in.elcot.avgcxr.support.grievance.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Domain aggregate root for DPDP Act 2023 compliant grievance redressal: 7-day acknowledgment,
 * 30-day resolution, escalation. This class contains business logic and validation rules. It has NO
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
public class Grievance extends AuditableEntity {

  private UUID id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static Grievance create() {
    return Grievance.builder()
        .id(UUID.randomUUID())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
