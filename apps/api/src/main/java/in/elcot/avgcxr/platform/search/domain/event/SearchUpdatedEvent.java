package in.elcot.avgcxr.platform.search.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SearchUpdatedEvent(UUID searchId, String field, String oldValue, String newValue, Instant occurredAt) {
    public static SearchUpdatedEvent from(UUID id, String field, String oldVal, String newVal) {
        return new SearchUpdatedEvent(id, field, oldVal, newVal, Instant.now());
    }
}
