package in.elcot.avgcxr.support.grievance.infrastructure.persistence.repository;

import in.elcot.avgcxr.support.grievance.infrastructure.persistence.entity.GrievanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaGrievanceRepository extends JpaRepository<GrievanceEntity, UUID> {
}
