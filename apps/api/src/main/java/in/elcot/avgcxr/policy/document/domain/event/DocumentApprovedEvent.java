package in.elcot.avgcxr.policy.document.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DocumentApprovedEvent(UUID documentId, String verifiedBy, Instant occurredAt) {
  public static DocumentApprovedEvent from(UUID id, String by) {
    return new DocumentApprovedEvent(id, by, Instant.now());
  }
}
