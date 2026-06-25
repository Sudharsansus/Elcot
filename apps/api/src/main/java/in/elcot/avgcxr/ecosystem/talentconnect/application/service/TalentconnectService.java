package in.elcot.avgcxr.ecosystem.talentconnect.application.service;

import in.elcot.avgcxr.ecosystem.talentconnect.application.command.CreateTalentconnectCommand;
import in.elcot.avgcxr.ecosystem.talentconnect.application.command.UpdateTalentconnectCommand;
import in.elcot.avgcxr.ecosystem.talentconnect.application.port.input.CreateTalentconnectUseCase;
import in.elcot.avgcxr.ecosystem.talentconnect.application.port.input.GetTalentconnectUseCase;
import in.elcot.avgcxr.ecosystem.talentconnect.application.port.output.TalentconnectRepositoryPort;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.exception.TalentconnectNotFoundException;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.Talentconnect;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentconnectId;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TalentconnectService implements CreateTalentconnectUseCase, GetTalentconnectUseCase {

  private final TalentconnectRepositoryPort repository;

  public TalentconnectService(TalentconnectRepositoryPort repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Talentconnect create(CreateTalentconnectCommand command) {
    Talentconnect entity = new Talentconnect(TalentconnectId.generate(), command.name());
    return repository.save(entity);
  }

  @Override
  public Optional<Talentconnect> findById(TalentconnectId id) {
    return repository.findById(id);
  }

  @Override
  public Talentconnect getById(TalentconnectId id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new TalentconnectNotFoundException(id.value()));
  }

  @Transactional
  public Talentconnect update(TalentconnectId id, UpdateTalentconnectCommand command) {
    Talentconnect entity = getById(id);
    entity.markUpdated();
    return repository.save(entity);
  }

  @Transactional
  public void delete(TalentconnectId id) {
    repository.deleteById(id);
  }
}
