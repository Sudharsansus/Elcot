package in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input;

import in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.response.FreelancerProfileResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetFreelancerProfileUseCase {
  FreelancerProfileResponse getById(UUID id);

  Page<FreelancerProfileResponse> findAll(Pageable pageable);
}
