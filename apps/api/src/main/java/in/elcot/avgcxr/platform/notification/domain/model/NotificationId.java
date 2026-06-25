package in.elcot.avgcxr.platform.notification.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record NotificationId(UUID value) implements Serializable {
  public static NotificationId generate() {
    return new NotificationId(UUID.randomUUID());
  }

  public static NotificationId of(String v) {
    return new NotificationId(UUID.fromString(v));
  }
}
