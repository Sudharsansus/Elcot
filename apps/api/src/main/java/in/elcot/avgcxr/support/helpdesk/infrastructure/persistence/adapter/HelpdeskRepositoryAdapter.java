package in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.adapter;

import in.elcot.avgcxr.support.helpdesk.application.port.output.HelpdeskRepositoryPort;
import in.elcot.avgcxr.support.helpdesk.domain.model.Helpdesk;
import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskId;
import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.entity.HelpdeskEntity;
import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.mapper.HelpdeskMapper;
import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.repository.JpaHelpdeskRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class HelpdeskRepositoryAdapter implements HelpdeskRepositoryPort {

    private final JpaHelpdeskRepository jpaRepo;
    private final HelpdeskMapper mapper;

    public HelpdeskRepositoryAdapter(JpaHelpdeskRepository jpaRepo, HelpdeskMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Helpdesk save(Helpdesk entity) { return mapper.toDomain(jpaRepo.save(mapper.toEntity(entity))); }

    @Override
    public Optional<Helpdesk> findById(HelpdeskId id) { return jpaRepo.findById(id.toString()).map(mapper::toDomain); }

    @Override
    public void deleteById(HelpdeskId id) { jpaRepo.deleteById(id.toString()); }

    @Override
    public boolean existsById(HelpdeskId id) { return jpaRepo.existsById(id.toString()); }
}
