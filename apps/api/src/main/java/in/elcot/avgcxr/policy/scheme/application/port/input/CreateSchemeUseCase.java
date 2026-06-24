package in.elcot.avgcxr.policy.scheme.application.port.input;

import in.elcot.avgcxr.policy.scheme.api.rest.dto.response.SchemeResponse;
import java.util.UUID;
import in.elcot.avgcxr.policy.scheme.application.command.CreateSchemeCommand;

public interface CreateSchemeUseCase {
    SchemeResponse create(CreateSchemeCommand command);
    SchemeResponse updateStatus(UUID id, String newStatus);
}
