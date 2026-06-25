package in.elcot.avgcxr.policy.application.application.port.input;

import in.elcot.avgcxr.policy.application.api.rest.dto.response.ApplicationResponse;
import in.elcot.avgcxr.policy.application.application.command.CreateApplicationCommand;
import java.util.UUID;

public interface CreateApplicationUseCase {
  ApplicationResponse create(CreateApplicationCommand command);

  ApplicationResponse createDraft(CreateApplicationCommand command);

  UUID createReturningId(CreateApplicationCommand command);
}
