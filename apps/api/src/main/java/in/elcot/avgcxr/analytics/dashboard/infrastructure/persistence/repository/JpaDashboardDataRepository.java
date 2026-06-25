package in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.repository;

import in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.entity.DashboardDataEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDashboardDataRepository extends JpaRepository<DashboardDataEntity, UUID> {}
