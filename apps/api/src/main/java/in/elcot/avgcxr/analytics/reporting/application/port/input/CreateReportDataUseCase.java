package in.elcot.avgcxr.analytics.reporting.application.port.input;

import in.elcot.avgcxr.analytics.reporting.api.rest.dto.response.ReportDataResponse;
import in.elcot.avgcxr.analytics.reporting.application.command.CreateReportDataCommand;

public interface CreateReportDataUseCase {
  ReportDataResponse create(CreateReportDataCommand command);
}
