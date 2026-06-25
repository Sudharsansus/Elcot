package in.elcot.avgcxr.platform.user.domain.event;

import java.time.Instant;
import java.util.UUID;

public record UserCreatedEvent(
    UUID userId, String username, String email, String mobileNumber, Instant occurredAt) {
  public static UserCreatedEvent from(UUID id, String u, String e, String m) {
    return new UserCreatedEvent(id, u, e, m, Instant.now());
  }
}
