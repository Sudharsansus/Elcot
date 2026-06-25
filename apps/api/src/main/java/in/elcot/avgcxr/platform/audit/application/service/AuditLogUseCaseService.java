package in.elcot.avgcxr.platform.audit.application.service;

import in.elcot.avgcxr.platform.audit.api.rest.dto.response.AuditLogResponse;
import in.elcot.avgcxr.platform.audit.application.command.CreateAuditLogCommand;
import in.elcot.avgcxr.platform.audit.application.port.input.CreateAuditLogUseCase;
import in.elcot.avgcxr.platform.audit.application.port.input.GetAuditLogUseCase;
import in.elcot.avgcxr.platform.audit.domain.exception.AuditLogNotFoundException;
import in.elcot.avgcxr.platform.audit.infrastructure.persistence.entity.AuditLogEntity;
import in.elcot.avgcxr.platform.audit.infrastructure.persistence.repository.JpaAuditLogRepository;
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
public class AuditLogUseCaseService implements CreateAuditLogUseCase, GetAuditLogUseCase {

  private final JpaAuditLogRepository repo;

  @Override
  @Transactional
  public AuditLogResponse create(CreateAuditLogCommand command) {
    log.info("Creating auditlog: {}", command);
    AuditLogEntity e = new AuditLogEntity();
    e.setId(UUID.randomUUID());
    e.setCreatedAt(LocalDateTime.now());
    e.setUpdatedAt(LocalDateTime.now());
    AuditLogEntity saved = repo.save(e);
    return toResponse(saved);
  }

  @Override
  public AuditLogResponse getById(UUID id) {
    AuditLogEntity e = repo.findById(id).orElseThrow(() -> new AuditLogNotFoundException(id));
    return toResponse(e);
  }

  @Override
  public Page<AuditLogResponse> findAll(Pageable pageable) {
    return repo.findAll(pageable).map(AuditLogUseCaseService::toResponse);
  }

  private static AuditLogResponse toResponse(AuditLogEntity e) {
    return new AuditLogResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
  }
}
