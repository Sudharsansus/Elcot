package in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.mapper;

import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.Freelancerregistry;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerregistryId;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.entity.FreelancerregistryEntity;
import org.springframework.stereotype.Component;

@Component
public class FreelancerregistryMapper {
    public FreelancerregistryEntity toEntity(Freelancerregistry domain) {
        FreelancerregistryEntity e = new FreelancerregistryEntity();
        e.setId(domain.getId().toString());
        e.setName("name");
        e.setDescription("description");
        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());
        return e;
    }

    public Freelancerregistry toDomain(FreelancerregistryEntity entity) {
        return new Freelancerregistry(FreelancerregistryId.of(entity.getId()), entity.getName() != null ? entity.getName() : "");
    }
}
