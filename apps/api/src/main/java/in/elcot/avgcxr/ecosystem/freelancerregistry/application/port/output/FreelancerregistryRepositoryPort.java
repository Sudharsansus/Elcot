package in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.output;

import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.Freelancerregistry;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerregistryId;
import java.util.Optional;

/** Output port for Freelancerregistry persistence - implemented by infrastructure adapter */
public interface FreelancerregistryRepositoryPort {
    Freelancerregistry save(Freelancerregistry entity);
    Optional<Freelancerregistry> findById(FreelancerregistryId id);
    void deleteById(FreelancerregistryId id);
    boolean existsById(FreelancerregistryId id);
}
