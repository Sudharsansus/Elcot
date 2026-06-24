package in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input;

import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.Freelancerregistry;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerregistryId;
import java.util.Optional;

public interface GetFreelancerregistryUseCase {
    Optional<Freelancerregistry> findById(FreelancerregistryId id);
    Freelancerregistry getById(FreelancerregistryId id);
}
