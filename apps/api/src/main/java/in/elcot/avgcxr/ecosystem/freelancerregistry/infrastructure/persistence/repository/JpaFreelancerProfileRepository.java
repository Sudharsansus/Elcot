package in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.repository;

import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.entity.FreelancerProfileEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFreelancerProfileRepository
    extends JpaRepository<FreelancerProfileEntity, UUID> {}
