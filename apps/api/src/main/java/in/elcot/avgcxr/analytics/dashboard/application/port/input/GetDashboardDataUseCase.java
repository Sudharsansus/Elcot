package in.elcot.avgcxr.analytics.dashboard.application.port.input;

import in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response.DashboardDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetDashboardDataUseCase {
    DashboardDataResponse getById(UUID id);
    Page<DashboardDataResponse> findAll(Pageable pageable);
}
