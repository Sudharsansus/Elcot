package in.elcot.avgcxr.platform.notification.domain.event;

import java.time.Instant;
import java.util.UUID;

public record NotificationUpdatedEvent(UUID notificationId, String status, Instant occurredAt) {
  public static NotificationUpdatedEvent from(UUID id, String status) {
    return new NotificationUpdatedEvent(id, status, Instant.now());
  }
}
