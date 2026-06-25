package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Domain aggregate root for Freelancer registry with rate-based filtering, portfolio links, and
 * availability management. This class contains business logic and validation rules. It has NO
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
public class FreelancerProfile extends AuditableEntity {

  private UUID id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static FreelancerProfile create() {
    return FreelancerProfile.builder()
        .id(UUID.randomUUID())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
