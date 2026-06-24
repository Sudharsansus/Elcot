package in.elcot.avgcxr.platform.notification.application.command;

import java.util.Map;

public record CreateNotificationCommand(
    Map<String, Object> fields
) {}
