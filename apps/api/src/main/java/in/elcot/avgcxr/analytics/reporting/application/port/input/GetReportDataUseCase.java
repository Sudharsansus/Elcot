package in.elcot.avgcxr.analytics.reporting.application.port.input;

import in.elcot.avgcxr.analytics.reporting.api.rest.dto.response.ReportDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetReportDataUseCase {
    ReportDataResponse getById(UUID id);
    Page<ReportDataResponse> findAll(Pageable pageable);
}
