package in.elcot.avgcxr.policy.document.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DocumentSubmittedEvent(UUID documentId, String applicationId, Instant occurredAt) {
    public static DocumentSubmittedEvent from(UUID docId, String appId) { return new DocumentSubmittedEvent(docId, appId, Instant.now()); }
}
