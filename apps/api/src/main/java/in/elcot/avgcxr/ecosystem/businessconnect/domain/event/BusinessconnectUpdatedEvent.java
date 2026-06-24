package in.elcot.avgcxr.ecosystem.businessconnect.domain.event;

import java.time.Instant;
import java.util.UUID;

public record BusinessconnectUpdatedEvent(UUID businessconnectId, String field, String oldValue, String newValue, Instant occurredAt) {
    public static BusinessconnectUpdatedEvent from(UUID id, String field, String oldVal, String newVal) {
        return new BusinessconnectUpdatedEvent(id, field, oldVal, newVal, Instant.now());
    }
}
