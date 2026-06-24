package in.elcot.avgcxr.support.grievance.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class GrievanceAlreadyExistsException extends ConflictException {
    public GrievanceAlreadyExistsException(String field, String value) {
        super("GRIEVANCE_DUPLICATE", "Grievance already exists with " + field + ": " + value);
    }
}
