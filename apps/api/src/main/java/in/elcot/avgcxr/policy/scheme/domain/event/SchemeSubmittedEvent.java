package in.elcot.avgcxr.policy.scheme.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SchemeSubmittedEvent(UUID schemeId, String submittedBy, Instant occurredAt) {
  public static SchemeSubmittedEvent from(UUID id, String by) {
    return new SchemeSubmittedEvent(id, by, Instant.now());
  }
}
