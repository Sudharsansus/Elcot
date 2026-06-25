package in.elcot.avgcxr.platform.audit.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class AuditLogCreatedEvent extends DomainEvent {
  public AuditLogCreatedEvent(UUID entityId) {
    super(
        UUID.randomUUID(),
        "AUDITLOG_CREATED",
        LocalDateTime.now(),
        entityId.toString(),
        Map.of("entityId", entityId.toString()));
  }
}
