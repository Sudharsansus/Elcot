package in.elcot.avgcxr.platform.notification.application.port.output;

import in.elcot.avgcxr.platform.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepositoryPort {
    Notification save(Notification entity);
    Optional<Notification> findById(UUID id);
    Page<Notification> findAll(Pageable pageable);
    void deleteById(UUID id);
}
