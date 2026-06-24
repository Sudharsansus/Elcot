package in.elcot.avgcxr.analytics.dashboard.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class DashboardDataAlreadyExistsException extends ConflictException {
    public DashboardDataAlreadyExistsException(String field, String value) {
        super("DASHBOARDDATA_DUPLICATE", "DashboardData already exists with " + field + ": " + value);
    }
}
