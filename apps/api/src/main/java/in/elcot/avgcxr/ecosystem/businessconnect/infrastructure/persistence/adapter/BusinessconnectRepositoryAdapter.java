package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.adapter;

import in.elcot.avgcxr.ecosystem.businessconnect.application.port.output.BusinessconnectRepositoryPort;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Businessconnect;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.BusinessconnectId;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.entity.BusinessconnectEntity;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.mapper.BusinessconnectMapper;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.repository.JpaBusinessconnectRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class BusinessconnectRepositoryAdapter implements BusinessconnectRepositoryPort {

    private final JpaBusinessconnectRepository jpaRepo;
    private final BusinessconnectMapper mapper;

    public BusinessconnectRepositoryAdapter(JpaBusinessconnectRepository jpaRepo, BusinessconnectMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Businessconnect save(Businessconnect entity) { return mapper.toDomain(jpaRepo.save(mapper.toEntity(entity))); }

    @Override
    public Optional<Businessconnect> findById(BusinessconnectId id) { return jpaRepo.findById(id.toString()).map(mapper::toDomain); }

    @Override
    public void deleteById(BusinessconnectId id) { jpaRepo.deleteById(id.toString()); }

    @Override
    public boolean existsById(BusinessconnectId id) { return jpaRepo.existsById(id.toString()); }
}
