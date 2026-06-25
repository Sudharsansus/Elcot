package in.elcot.avgcxr.support.helpdesk.domain.event;

import java.time.Instant;
import java.util.UUID;

public record HelpdeskCreatedEvent(UUID helpdeskId, String action, Instant occurredAt) {
  public static HelpdeskCreatedEvent from(UUID id) {
    return new HelpdeskCreatedEvent(id, "CREATED", Instant.now());
  }
}
