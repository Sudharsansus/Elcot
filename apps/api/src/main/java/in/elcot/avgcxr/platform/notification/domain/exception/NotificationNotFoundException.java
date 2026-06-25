package in.elcot.avgcxr.platform.notification.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class NotificationNotFoundException extends NotFoundException {
  public NotificationNotFoundException(UUID id) {
    super("NOTIFICATION_NOT_FOUND", "Notification not found with id: " + id);
  }
}
