package in.elcot.avgcxr.support.helpdesk.domain.event;

import java.time.Instant;
import java.util.UUID;

public record HelpdeskUpdatedEvent(UUID helpdeskId, String field, String oldValue, String newValue, Instant occurredAt) {
    public static HelpdeskUpdatedEvent from(UUID id, String field, String oldVal, String newVal) {
        return new HelpdeskUpdatedEvent(id, field, oldVal, newVal, Instant.now());
    }
}
