package in.elcot.avgcxr.analytics.dashboard.application.port.output;

import in.elcot.avgcxr.analytics.dashboard.domain.model.DashboardData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for dashboard data persistence. Implemented by
 * DashboardDataRepositoryAdapter in infrastructure/persistence/.
 */
public interface DashboardDataRepositoryPort {
    DashboardData save(DashboardData data);
    Optional<DashboardData> findById(UUID id);
    Page<DashboardData> findAll(Pageable pageable);
    void deleteById(UUID id);
}