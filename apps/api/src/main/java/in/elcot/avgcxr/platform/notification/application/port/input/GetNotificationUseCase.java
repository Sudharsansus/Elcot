package in.elcot.avgcxr.platform.notification.application.port.input;

import in.elcot.avgcxr.platform.notification.api.rest.dto.response.NotificationResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetNotificationUseCase {
  NotificationResponse getById(UUID id);

  Page<NotificationResponse> findAll(Pageable pageable);
}
