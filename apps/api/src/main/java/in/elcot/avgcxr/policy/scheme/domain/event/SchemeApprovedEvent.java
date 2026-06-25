package in.elcot.avgcxr.policy.scheme.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SchemeApprovedEvent(UUID schemeId, String approvedBy, Instant occurredAt) {
  public static SchemeApprovedEvent from(UUID id, String by) {
    return new SchemeApprovedEvent(id, by, Instant.now());
  }
}
