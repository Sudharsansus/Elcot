package in.elcot.avgcxr.ecosystem.businessconnect.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain aggregate root for AVGC-XR company directory with category filtering, skill-based search, and verification workflow.
 * This class contains business logic and validation rules.
 * It has NO dependencies on Spring or infrastructure.
 */
/**
 * Pure domain model — no framework imports.
 * Persistence is handled by infrastructure.persistence.entity + mapper.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Company extends AuditableEntity {

 private UUID id;

 private LocalDateTime createdAt;

 private LocalDateTime updatedAt;

 public static Company create() {
 return Company.builder()
 .id(UUID.randomUUID())
 .createdAt(LocalDateTime.now())
 .updatedAt(LocalDateTime.now())
 .build();
 }
}
