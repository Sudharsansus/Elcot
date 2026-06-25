package in.elcot.avgcxr.platform.auth.application.port.input;

import in.elcot.avgcxr.platform.auth.api.rest.dto.response.AuthResponse;
import in.elcot.avgcxr.platform.auth.application.command.RegisterUserCommand;

public interface CreateUserUseCase {
  AuthResponse create(RegisterUserCommand command);
}
