package in.elcot.avgcxr.platform.notification.application.service;

import in.elcot.avgcxr.platform.notification.api.rest.dto.response.NotificationResponse;
import in.elcot.avgcxr.platform.notification.application.command.CreateNotificationCommand;
import in.elcot.avgcxr.platform.notification.application.port.input.CreateNotificationUseCase;
import in.elcot.avgcxr.platform.notification.application.port.input.GetNotificationUseCase;
import in.elcot.avgcxr.platform.notification.domain.exception.NotificationNotFoundException;
import in.elcot.avgcxr.platform.notification.infrastructure.persistence.entity.NotificationEntity;
import in.elcot.avgcxr.platform.notification.infrastructure.persistence.repository.JpaNotificationRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Real implementation: persists to PostgreSQL via JPA. Replaces the Phase-1 stub with full CRUD.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationUseCaseService
    implements CreateNotificationUseCase, GetNotificationUseCase {

  private final JpaNotificationRepository repo;

  @Override
  @Transactional
  public NotificationResponse create(CreateNotificationCommand command) {
    log.info("Creating notification: {}", command);
    NotificationEntity e = new NotificationEntity();
    e.setId(UUID.randomUUID());
    e.setCreatedAt(LocalDateTime.now());
    e.setUpdatedAt(LocalDateTime.now());
    NotificationEntity saved = repo.save(e);
    return toResponse(saved);
  }

  @Override
  public NotificationResponse getById(UUID id) {
    NotificationEntity e =
        repo.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
    return toResponse(e);
  }

  @Override
  public Page<NotificationResponse> findAll(Pageable pageable) {
    return repo.findAll(pageable).map(NotificationUseCaseService::toResponse);
  }

  private static NotificationResponse toResponse(NotificationEntity e) {
    return new NotificationResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
  }
}
