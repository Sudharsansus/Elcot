package in.elcot.avgcxr.platform.notification.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.notification.application.port.output.NotificationRepositoryPort;
import in.elcot.avgcxr.platform.notification.domain.model.Notification;
import in.elcot.avgcxr.platform.notification.infrastructure.persistence.mapper.NotificationMapper;
import in.elcot.avgcxr.platform.notification.infrastructure.persistence.repository.JpaNotificationRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {
  private final JpaNotificationRepository jpaRepository;

  @Override
  public Notification save(Notification entity) {
    return NotificationMapper.toDomain(jpaRepository.save(NotificationMapper.toEntity(entity)));
  }

  @Override
  public Optional<Notification> findById(UUID id) {
    return jpaRepository.findById(id).map(NotificationMapper::toDomain);
  }

  @Override
  public Page<Notification> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(NotificationMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
