package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class FreelancerProfileNotFoundException extends NotFoundException {
    public FreelancerProfileNotFoundException(UUID id) {
        super("FREELANCERPROFILE_NOT_FOUND", "FreelancerProfile not found with id: " + id);
    }
}
