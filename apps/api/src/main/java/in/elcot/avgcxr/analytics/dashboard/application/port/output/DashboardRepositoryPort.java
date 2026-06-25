package in.elcot.avgcxr.analytics.dashboard.application.port.output;

import java.util.Map;

public interface DashboardRepositoryPort {
  Map<String, Long> getApplicationsByStatus();

  Map<String, Long> getApplicationsByDistrict();

  Map<String, Long> getApplicationsBySubSector();

  long countTotalApplications();

  long countPendingReviews();

  long countApprovedToday();

  long sumTotalDisbursed();
}
