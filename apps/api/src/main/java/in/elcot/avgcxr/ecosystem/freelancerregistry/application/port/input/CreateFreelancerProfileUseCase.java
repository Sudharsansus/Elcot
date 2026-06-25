package in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input;

import in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.response.FreelancerProfileResponse;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.command.CreateFreelancerProfileCommand;

public interface CreateFreelancerProfileUseCase {
  FreelancerProfileResponse create(CreateFreelancerProfileCommand command);
}
