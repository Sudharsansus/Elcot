package in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.repository;

import in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.entity.DashboardDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaDashboardDataRepository extends JpaRepository<DashboardDataEntity, UUID> {
}
