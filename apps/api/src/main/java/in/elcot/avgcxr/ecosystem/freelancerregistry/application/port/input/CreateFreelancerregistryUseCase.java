package in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input;

import in.elcot.avgcxr.ecosystem.freelancerregistry.application.command.CreateFreelancerregistryCommand;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.Freelancerregistry;

public interface CreateFreelancerregistryUseCase {
    Freelancerregistry create(CreateFreelancerregistryCommand command);
}
