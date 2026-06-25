package in.elcot.avgcxr.platform.audit.domain.event;

import java.time.Instant;
import java.util.UUID;

public record AuditCreatedEvent(UUID auditId, String action, Instant occurredAt) {
  public static AuditCreatedEvent from(UUID id) {
    return new AuditCreatedEvent(id, "CREATED", Instant.now());
  }
}
