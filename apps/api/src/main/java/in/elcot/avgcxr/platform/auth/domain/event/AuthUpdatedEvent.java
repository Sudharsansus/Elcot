package in.elcot.avgcxr.platform.auth.domain.event;

import java.time.Instant;
import java.util.UUID;

public record AuthUpdatedEvent(UUID userId, String action, String details, Instant occurredAt) {
    public static AuthUpdatedEvent passwordChanged(UUID id) { return new AuthUpdatedEvent(id, "PASSWORD_CHANGE", "User changed password", Instant.now()); }
}
