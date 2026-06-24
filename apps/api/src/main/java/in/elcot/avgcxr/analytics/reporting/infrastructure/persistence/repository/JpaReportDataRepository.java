package in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.repository;

import in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.entity.ReportDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaReportDataRepository extends JpaRepository<ReportDataEntity, UUID> {
}
