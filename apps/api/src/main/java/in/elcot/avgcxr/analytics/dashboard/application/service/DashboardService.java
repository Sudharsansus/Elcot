package in.elcot.avgcxr.analytics.dashboard.application.service;

import in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response.DashboardResponse;
import in.elcot.avgcxr.analytics.dashboard.application.port.input.GetDashboardUseCase;
import in.elcot.avgcxr.analytics.dashboard.application.port.output.DashboardRepositoryPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DashboardService implements GetDashboardUseCase {
    private final DashboardRepositoryPort repo;
    public DashboardService(DashboardRepositoryPort repo) { this.repo = repo; }

    @Override
    @Cacheable(value = "dashboard-stats", key = "'all'")
    public DashboardResponse getDashboardStats() {
        return new DashboardResponse(repo.countTotalApplications(), repo.countPendingReviews(), repo.countApprovedToday(), repo.sumTotalDisbursed(), repo.getApplicationsByStatus(), repo.getApplicationsByDistrict(), repo.getApplicationsBySubSector());
    }

    @Override
    @Cacheable(value = "dashboard-stats", key = "#district")
    public DashboardResponse getDistrictStats(String district) {
        var byDistrict = repo.getApplicationsByDistrict();
        long districtTotal = byDistrict.getOrDefault(district, 0L);
        return new DashboardResponse(districtTotal, 0, 0, 0, repo.getApplicationsByStatus(), byDistrict, repo.getApplicationsBySubSector());
    }
}

