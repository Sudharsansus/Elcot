package in.elcot.avgcxr.support.grievance.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class GrievanceNotFoundException extends NotFoundException {
    public GrievanceNotFoundException(UUID id) {
        super("GRIEVANCE_NOT_FOUND", "Grievance not found with id: " + id);
    }
}
