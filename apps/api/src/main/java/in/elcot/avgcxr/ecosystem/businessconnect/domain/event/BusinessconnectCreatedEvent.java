package in.elcot.avgcxr.ecosystem.businessconnect.domain.event;

import java.time.Instant;
import java.util.UUID;

public record BusinessconnectCreatedEvent(
    UUID businessconnectId, String action, Instant occurredAt) {
  public static BusinessconnectCreatedEvent from(UUID id) {
    return new BusinessconnectCreatedEvent(id, "CREATED", Instant.now());
  }
}
