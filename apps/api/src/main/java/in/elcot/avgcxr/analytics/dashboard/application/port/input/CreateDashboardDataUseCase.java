package in.elcot.avgcxr.analytics.dashboard.application.port.input;

import in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response.DashboardDataResponse;
import in.elcot.avgcxr.analytics.dashboard.application.command.CreateDashboardDataCommand;

public interface CreateDashboardDataUseCase {
    DashboardDataResponse create(CreateDashboardDataCommand command);
}
