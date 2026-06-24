package in.elcot.avgcxr.platform.auth.domain.event;

import java.time.Instant;
import java.util.UUID;

public record AuthCreatedEvent(UUID userId, String action, Instant occurredAt) {
    public static AuthCreatedEvent login(UUID id) { return new AuthCreatedEvent(id, "LOGIN", Instant.now()); }
    public static AuthCreatedEvent logout(UUID id) { return new AuthCreatedEvent(id, "LOGOUT", Instant.now()); }
}
