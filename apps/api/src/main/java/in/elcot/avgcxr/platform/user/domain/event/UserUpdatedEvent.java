package in.elcot.avgcxr.platform.user.domain.event;

import java.time.Instant;
import java.util.UUID;

public record UserUpdatedEvent(
    UUID userId, String field, String oldValue, String newValue, Instant occurredAt) {
  public static UserUpdatedEvent from(UUID id, String f, String o, String n) {
    return new UserUpdatedEvent(id, f, o, n, Instant.now());
  }
}
