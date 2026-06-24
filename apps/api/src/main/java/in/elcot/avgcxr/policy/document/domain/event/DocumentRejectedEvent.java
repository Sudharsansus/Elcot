package in.elcot.avgcxr.policy.document.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DocumentRejectedEvent(UUID documentId, String rejectedBy, String reason, Instant occurredAt) {
    public static DocumentRejectedEvent from(UUID id, String by, String reason) { return new DocumentRejectedEvent(id, by, reason, Instant.now()); }
}
