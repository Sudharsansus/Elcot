package in.elcot.avgcxr.platform.file.domain.event;

import java.time.Instant;
import java.util.UUID;

public record FileUpdatedEvent(UUID fileId, String field, String oldValue, String newValue, Instant occurredAt) {
    public static FileUpdatedEvent from(UUID id, String f, String o, String n) { return new FileUpdatedEvent(id, f, o, n, Instant.now()); }
}
