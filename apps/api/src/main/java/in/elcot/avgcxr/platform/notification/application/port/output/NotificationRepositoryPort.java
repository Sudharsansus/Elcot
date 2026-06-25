package in.elcot.avgcxr.platform.notification.application.port.output;

import in.elcot.avgcxr.platform.notification.domain.model.Notification;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepositoryPort {
  Notification save(Notification entity);

  Optional<Notification> findById(UUID id);

  Page<Notification> findAll(Pageable pageable);

  void deleteById(UUID id);
}
