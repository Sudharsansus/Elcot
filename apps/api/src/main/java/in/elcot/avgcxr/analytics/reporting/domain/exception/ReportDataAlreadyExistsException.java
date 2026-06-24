package in.elcot.avgcxr.analytics.reporting.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class ReportDataAlreadyExistsException extends ConflictException {
    public ReportDataAlreadyExistsException(String field, String value) {
        super("REPORTDATA_DUPLICATE", "ReportData already exists with " + field + ": " + value);
    }
}
