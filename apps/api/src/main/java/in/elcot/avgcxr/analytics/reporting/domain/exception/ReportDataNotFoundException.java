package in.elcot.avgcxr.analytics.reporting.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class ReportDataNotFoundException extends NotFoundException {
  public ReportDataNotFoundException(UUID id) {
    super("REPORTDATA_NOT_FOUND", "ReportData not found with id: " + id);
  }
}
