package in.elcot.avgcxr.platform.audit.domain.event;

import java.time.Instant;
import java.util.UUID;

public record AuditUpdatedEvent(UUID auditId, String field, String oldValue, String newValue, Instant occurredAt) {
    public static AuditUpdatedEvent from(UUID id, String field, String oldVal, String newVal) {
        return new AuditUpdatedEvent(id, field, oldVal, newVal, Instant.now());
    }
}
