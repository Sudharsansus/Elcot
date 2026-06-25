package in.elcot.avgcxr.policy.scheme.application.port.input;

import in.elcot.avgcxr.policy.scheme.api.rest.dto.response.SchemeResponse;
import in.elcot.avgcxr.policy.scheme.application.command.CreateSchemeCommand;
import java.util.UUID;

public interface CreateSchemeUseCase {
  SchemeResponse create(CreateSchemeCommand command);

  SchemeResponse updateStatus(UUID id, String newStatus);
}
