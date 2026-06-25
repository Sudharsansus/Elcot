package in.elcot.avgcxr.ecosystem.freelancerregistry.application.service;

import in.elcot.avgcxr.ecosystem.freelancerregistry.application.command.CreateFreelancerregistryCommand;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.command.UpdateFreelancerregistryCommand;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input.CreateFreelancerregistryUseCase;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input.GetFreelancerregistryUseCase;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.output.FreelancerregistryRepositoryPort;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.exception.FreelancerregistryNotFoundException;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.Freelancerregistry;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerregistryId;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FreelancerregistryService
    implements CreateFreelancerregistryUseCase, GetFreelancerregistryUseCase {

  private final FreelancerregistryRepositoryPort repository;

  public FreelancerregistryService(FreelancerregistryRepositoryPort repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Freelancerregistry create(CreateFreelancerregistryCommand command) {
    Freelancerregistry entity =
        new Freelancerregistry(FreelancerregistryId.generate(), command.name());
    return repository.save(entity);
  }

  @Override
  public Optional<Freelancerregistry> findById(FreelancerregistryId id) {
    return repository.findById(id);
  }

  @Override
  public Freelancerregistry getById(FreelancerregistryId id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new FreelancerregistryNotFoundException(id.value()));
  }

  @Transactional
  public Freelancerregistry update(
      FreelancerregistryId id, UpdateFreelancerregistryCommand command) {
    Freelancerregistry entity = getById(id);
    entity.markUpdated();
    return repository.save(entity);
  }

  @Transactional
  public void delete(FreelancerregistryId id) {
    repository.deleteById(id);
  }
}
