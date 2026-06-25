package in.elcot.avgcxr.analytics.dashboard.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class DashboardDataNotFoundException extends NotFoundException {
  public DashboardDataNotFoundException(UUID id) {
    super("DASHBOARDDATA_NOT_FOUND", "DashboardData not found with id: " + id);
  }
}
