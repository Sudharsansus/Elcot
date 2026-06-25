package in.elcot.avgcxr.support.helpdesk.application.service;

import in.elcot.avgcxr.support.helpdesk.application.command.CreateHelpdeskCommand;
import in.elcot.avgcxr.support.helpdesk.application.command.UpdateHelpdeskCommand;
import in.elcot.avgcxr.support.helpdesk.application.port.input.CreateHelpdeskUseCase;
import in.elcot.avgcxr.support.helpdesk.application.port.input.GetHelpdeskUseCase;
import in.elcot.avgcxr.support.helpdesk.application.port.output.HelpdeskRepositoryPort;
import in.elcot.avgcxr.support.helpdesk.domain.exception.HelpdeskNotFoundException;
import in.elcot.avgcxr.support.helpdesk.domain.model.Helpdesk;
import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskId;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HelpdeskService implements CreateHelpdeskUseCase, GetHelpdeskUseCase {

  private final HelpdeskRepositoryPort repository;

  public HelpdeskService(HelpdeskRepositoryPort repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Helpdesk create(CreateHelpdeskCommand command) {
    Helpdesk entity = new Helpdesk(HelpdeskId.generate(), command.name());
    return repository.save(entity);
  }

  @Override
  public Optional<Helpdesk> findById(HelpdeskId id) {
    return repository.findById(id);
  }

  @Override
  public Helpdesk getById(HelpdeskId id) {
    return repository.findById(id).orElseThrow(() -> new HelpdeskNotFoundException(id.value()));
  }

  @Transactional
  public Helpdesk update(HelpdeskId id, UpdateHelpdeskCommand command) {
    Helpdesk entity = getById(id);
    entity.markUpdated();
    return repository.save(entity);
  }

  @Transactional
  public void delete(HelpdeskId id) {
    repository.deleteById(id);
  }
}
