package in.elcot.avgcxr.analytics.dashboard.application.port.input;

import in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response.DashboardResponse;

public interface GetDashboardUseCase {
    DashboardResponse getDashboardStats();
    DashboardResponse getDistrictStats(String district);
}
