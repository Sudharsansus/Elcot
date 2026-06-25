package in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.output;

import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreelancerProfileRepositoryPort {
  FreelancerProfile save(FreelancerProfile entity);

  Optional<FreelancerProfile> findById(UUID id);

  Page<FreelancerProfile> findAll(Pageable pageable);

  void deleteById(UUID id);
}
