package in.elcot.avgcxr.platform.user.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain aggregate root for Extended user profiles with skills, company details, and portfolio information.
 * This class contains business logic and validation rules.
 * It has NO dependencies on Spring or infrastructure.
 */
/**
 * Pure domain model — no framework imports.
 * Persistence is handled by infrastructure.persistence.entity + mapper.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class UserProfile extends AuditableEntity {

 private UUID id;

 private LocalDateTime createdAt;

 private LocalDateTime updatedAt;

 public static UserProfile create() {
 return UserProfile.builder()
 .id(UUID.randomUUID())
 .createdAt(LocalDateTime.now())
 .updatedAt(LocalDateTime.now())
 .build();
 }
}
