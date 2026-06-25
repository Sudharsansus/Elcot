package in.elcot.avgcxr.analytics.dashboard.application.port.input;

import in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response.DashboardDataResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetDashboardDataUseCase {
  DashboardDataResponse getById(UUID id);

  Page<DashboardDataResponse> findAll(Pageable pageable);
}
