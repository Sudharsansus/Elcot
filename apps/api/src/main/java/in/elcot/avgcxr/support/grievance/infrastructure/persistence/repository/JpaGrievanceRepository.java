package in.elcot.avgcxr.support.grievance.infrastructure.persistence.repository;

import in.elcot.avgcxr.support.grievance.infrastructure.persistence.entity.GrievanceEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGrievanceRepository extends JpaRepository<GrievanceEntity, UUID> {}
