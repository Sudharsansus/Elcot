package in.elcot.avgcxr.platform.notification.application.port.input;

import in.elcot.avgcxr.platform.notification.api.rest.dto.response.NotificationResponse;
import in.elcot.avgcxr.platform.notification.application.command.CreateNotificationCommand;

public interface CreateNotificationUseCase {
    NotificationResponse create(CreateNotificationCommand command);
}
