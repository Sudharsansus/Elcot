package in.elcot.avgcxr.platform.search.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SearchCreatedEvent(UUID searchId, String action, Instant occurredAt) {
    public static SearchCreatedEvent from(UUID id) { return new SearchCreatedEvent(id, "CREATED", Instant.now()); }
}
