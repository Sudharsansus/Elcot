package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.event;

import java.time.Instant;
import java.util.UUID;

public record FreelancerregistryUpdatedEvent(
    UUID freelancerregistryId, String field, String oldValue, String newValue, Instant occurredAt) {
  public static FreelancerregistryUpdatedEvent from(
      UUID id, String field, String oldVal, String newVal) {
    return new FreelancerregistryUpdatedEvent(id, field, oldVal, newVal, Instant.now());
  }
}
