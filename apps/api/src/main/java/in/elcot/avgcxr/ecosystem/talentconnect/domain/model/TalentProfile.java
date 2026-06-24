package in.elcot.avgcxr.ecosystem.talentconnect.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain aggregate root for Talent professional registry with skill matching, experience filtering, and availability status.
 * This class contains business logic and validation rules.
 * It has NO dependencies on Spring or infrastructure.
 */
/**
 * Pure domain model — no framework imports.
 * Persistence is handled by infrastructure.persistence.entity + mapper.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class TalentProfile extends AuditableEntity {

 private UUID id;

 private LocalDateTime createdAt;

 private LocalDateTime updatedAt;

 public static TalentProfile create() {
 return TalentProfile.builder()
 .id(UUID.randomUUID())
 .createdAt(LocalDateTime.now())
 .updatedAt(LocalDateTime.now())
 .build();
 }
}
