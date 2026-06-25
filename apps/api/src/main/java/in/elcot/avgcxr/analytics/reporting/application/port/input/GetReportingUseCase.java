package in.elcot.avgcxr.analytics.reporting.application.port.input;

import in.elcot.avgcxr.analytics.reporting.api.rest.dto.response.ReportingResponse;
import java.time.LocalDate;

public interface GetReportingUseCase {
  ReportingResponse generateApplicationReport(LocalDate from, LocalDate to, String district);

  ReportingResponse generateDisbursementReport(LocalDate from, LocalDate to);

  byte[] exportApplicationReportCsv(LocalDate from, LocalDate to);
}
