package in.elcot.avgcxr.platform.notification.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.notification.infrastructure.persistence.entity.NotificationEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, UUID> {}
