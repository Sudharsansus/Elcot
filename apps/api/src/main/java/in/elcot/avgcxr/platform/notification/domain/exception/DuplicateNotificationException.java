package in.elcot.avgcxr.platform.notification.domain.exception;



public class DuplicateNotificationException extends RuntimeException {
    public DuplicateNotificationException(String id) { super("Duplicate notification: " + id); }
}
