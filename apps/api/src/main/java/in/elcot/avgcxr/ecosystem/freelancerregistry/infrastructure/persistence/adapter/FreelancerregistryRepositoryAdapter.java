package in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.adapter;

import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.output.FreelancerregistryRepositoryPort;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.Freelancerregistry;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerregistryId;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.entity.FreelancerregistryEntity;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.mapper.FreelancerregistryMapper;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.repository.JpaFreelancerregistryRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class FreelancerregistryRepositoryAdapter implements FreelancerregistryRepositoryPort {

    private final JpaFreelancerregistryRepository jpaRepo;
    private final FreelancerregistryMapper mapper;

    public FreelancerregistryRepositoryAdapter(JpaFreelancerregistryRepository jpaRepo, FreelancerregistryMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Freelancerregistry save(Freelancerregistry entity) { return mapper.toDomain(jpaRepo.save(mapper.toEntity(entity))); }

    @Override
    public Optional<Freelancerregistry> findById(FreelancerregistryId id) { return jpaRepo.findById(id.toString()).map(mapper::toDomain); }

    @Override
    public void deleteById(FreelancerregistryId id) { jpaRepo.deleteById(id.toString()); }

    @Override
    public boolean existsById(FreelancerregistryId id) { return jpaRepo.existsById(id.toString()); }
}
