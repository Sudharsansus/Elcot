package in.elcot.avgcxr.policy.scheme.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SchemeRejectedEvent(
    UUID schemeId, String rejectedBy, String reason, Instant occurredAt) {
  public static SchemeRejectedEvent from(UUID id, String by, String reason) {
    return new SchemeRejectedEvent(id, by, reason, Instant.now());
  }
}
