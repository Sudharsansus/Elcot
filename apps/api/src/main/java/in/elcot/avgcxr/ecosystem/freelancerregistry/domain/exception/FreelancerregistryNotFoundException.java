package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.exception;

import java.util.UUID;

public class FreelancerregistryNotFoundException extends RuntimeException {
    public FreelancerregistryNotFoundException(UUID id) { super("Freelancerregistry not found with id: " + id); }
    public FreelancerregistryNotFoundException(String identifier) { super("Freelancerregistry not found: " + identifier); }
}
