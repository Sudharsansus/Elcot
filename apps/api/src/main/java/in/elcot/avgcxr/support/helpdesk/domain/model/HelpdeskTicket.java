package in.elcot.avgcxr.support.helpdesk.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain aggregate root for Helpdesk ticket management with priority-based routing, SLA tracking, and resolution workflow.
 * This class contains business logic and validation rules.
 * It has NO dependencies on Spring or infrastructure.
 */
/**
 * Pure domain model — no framework imports.
 * Persistence is handled by infrastructure.persistence.entity + mapper.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class HelpdeskTicket extends AuditableEntity {

 private UUID id;

 private LocalDateTime createdAt;

 private LocalDateTime updatedAt;

 public static HelpdeskTicket create() {
 return HelpdeskTicket.builder()
 .id(UUID.randomUUID())
 .createdAt(LocalDateTime.now())
 .updatedAt(LocalDateTime.now())
 .build();
 }
}
