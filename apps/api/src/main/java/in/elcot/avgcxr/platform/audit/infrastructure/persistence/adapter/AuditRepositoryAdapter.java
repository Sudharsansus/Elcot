package in.elcot.avgcxr.platform.audit.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.audit.application.port.output.AuditRepositoryPort;
import in.elcot.avgcxr.platform.audit.domain.model.Audit;
import in.elcot.avgcxr.platform.audit.domain.model.AuditId;
import in.elcot.avgcxr.platform.audit.infrastructure.persistence.mapper.AuditMapper;
import in.elcot.avgcxr.platform.audit.infrastructure.persistence.repository.JpaAuditRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AuditRepositoryAdapter implements AuditRepositoryPort {

  private final JpaAuditRepository jpaRepo;
  private final AuditMapper mapper;

  public AuditRepositoryAdapter(JpaAuditRepository jpaRepo, AuditMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Audit save(Audit entity) {
    return mapper.toDomain(jpaRepo.save(mapper.toEntity(entity)));
  }

  @Override
  public Optional<Audit> findById(AuditId id) {
    return jpaRepo.findById(id.toString()).map(mapper::toDomain);
  }

  @Override
  public void deleteById(AuditId id) {
    jpaRepo.deleteById(id.toString());
  }

  @Override
  public boolean existsById(AuditId id) {
    return jpaRepo.existsById(id.toString());
  }
}
