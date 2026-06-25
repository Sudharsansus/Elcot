package in.elcot.avgcxr.platform.audit.application.service;

import in.elcot.avgcxr.platform.audit.application.command.CreateAuditCommand;
import in.elcot.avgcxr.platform.audit.application.command.UpdateAuditCommand;
import in.elcot.avgcxr.platform.audit.application.port.input.CreateAuditUseCase;
import in.elcot.avgcxr.platform.audit.application.port.input.GetAuditUseCase;
import in.elcot.avgcxr.platform.audit.application.port.output.AuditRepositoryPort;
import in.elcot.avgcxr.platform.audit.domain.exception.AuditNotFoundException;
import in.elcot.avgcxr.platform.audit.domain.model.Audit;
import in.elcot.avgcxr.platform.audit.domain.model.AuditId;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuditService implements CreateAuditUseCase, GetAuditUseCase {

  private final AuditRepositoryPort repository;

  public AuditService(AuditRepositoryPort repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Audit create(CreateAuditCommand command) {
    Audit entity = new Audit(AuditId.generate(), command.name());
    return repository.save(entity);
  }

  @Override
  public Optional<Audit> findById(AuditId id) {
    return repository.findById(id);
  }

  @Override
  public Audit getById(AuditId id) {
    return repository.findById(id).orElseThrow(() -> new AuditNotFoundException(id.value()));
  }

  @Transactional
  public Audit update(AuditId id, UpdateAuditCommand command) {
    Audit entity = getById(id);
    entity.markUpdated();
    return repository.save(entity);
  }

  @Transactional
  public void delete(AuditId id) {
    repository.deleteById(id);
  }
}
