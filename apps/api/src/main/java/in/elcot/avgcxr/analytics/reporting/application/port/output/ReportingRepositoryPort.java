package in.elcot.avgcxr.analytics.reporting.application.port.output;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportingRepositoryPort {
  List<Map<String, Object>> getApplicationStats(LocalDate from, LocalDate to, String district);

  List<Map<String, Object>> getDisbursementStats(LocalDate from, LocalDate to);

  Map<String, Object> getApplicationSummary(LocalDate from, LocalDate to);
}
