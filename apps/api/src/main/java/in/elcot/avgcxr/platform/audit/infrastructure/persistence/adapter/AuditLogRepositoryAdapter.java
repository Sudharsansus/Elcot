package in.elcot.avgcxr.platform.audit.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.audit.application.port.output.AuditLogRepositoryPort;
import in.elcot.avgcxr.platform.audit.domain.model.AuditLog;
import in.elcot.avgcxr.platform.audit.infrastructure.persistence.mapper.AuditLogMapper;
import in.elcot.avgcxr.platform.audit.infrastructure.persistence.repository.JpaAuditLogRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditLogRepositoryAdapter implements AuditLogRepositoryPort {
  private final JpaAuditLogRepository jpaRepository;

  @Override
  public AuditLog save(AuditLog entity) {
    return AuditLogMapper.toDomain(jpaRepository.save(AuditLogMapper.toEntity(entity)));
  }

  @Override
  public Optional<AuditLog> findById(UUID id) {
    return jpaRepository.findById(id).map(AuditLogMapper::toDomain);
  }

  @Override
  public Page<AuditLog> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(AuditLogMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
