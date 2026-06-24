package in.elcot.avgcxr.platform.notification.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class NotificationAlreadyExistsException extends ConflictException {
    public NotificationAlreadyExistsException(String field, String value) {
        super("NOTIFICATION_DUPLICATE", "Notification already exists with " + field + ": " + value);
    }
}
