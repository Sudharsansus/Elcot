package in.elcot.avgcxr.ecosystem.businessconnect.application.service;

import in.elcot.avgcxr.ecosystem.businessconnect.application.command.CreateBusinessconnectCommand;
import in.elcot.avgcxr.ecosystem.businessconnect.application.command.UpdateBusinessconnectCommand;
import in.elcot.avgcxr.ecosystem.businessconnect.application.port.input.CreateBusinessconnectUseCase;
import in.elcot.avgcxr.ecosystem.businessconnect.application.port.input.GetBusinessconnectUseCase;
import in.elcot.avgcxr.ecosystem.businessconnect.application.port.output.BusinessconnectRepositoryPort;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.exception.BusinessconnectNotFoundException;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.exception.DuplicateBusinessconnectException;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Businessconnect;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.BusinessconnectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BusinessconnectService implements CreateBusinessconnectUseCase, GetBusinessconnectUseCase {

    private final BusinessconnectRepositoryPort repository;

    public BusinessconnectService(BusinessconnectRepositoryPort repository) { this.repository = repository; }

    @Override
    @Transactional
    public Businessconnect create(CreateBusinessconnectCommand command) {
        Businessconnect entity = new Businessconnect(BusinessconnectId.generate(), command.name());
        return repository.save(entity);
    }

    @Override
    public Optional<Businessconnect> findById(BusinessconnectId id) { return repository.findById(id); }

    @Override
    public Businessconnect getById(BusinessconnectId id) {
        return repository.findById(id).orElseThrow(() -> new BusinessconnectNotFoundException(id.value()));
    }

    @Transactional
    public Businessconnect update(BusinessconnectId id, UpdateBusinessconnectCommand command) {
        Businessconnect entity = getById(id);
        entity.markUpdated();
        return repository.save(entity);
    }

    @Transactional
    public void delete(BusinessconnectId id) { repository.deleteById(id); }
}
