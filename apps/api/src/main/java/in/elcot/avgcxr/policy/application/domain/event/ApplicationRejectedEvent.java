package in.elcot.avgcxr.policy.application.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ApplicationRejectedEvent(
    UUID applicationId, String rejectedBy, String reason, Instant occurredAt) {
  public static ApplicationRejectedEvent from(UUID id, String by, String reason) {
    return new ApplicationRejectedEvent(id, by, reason, Instant.now());
  }
}
