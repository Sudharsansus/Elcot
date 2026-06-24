package in.elcot.avgcxr.platform.user.application.port.input;

import in.elcot.avgcxr.platform.user.application.command.CreateUserCommand;
import in.elcot.avgcxr.platform.user.domain.model.User;

public interface CreateUserUseCase {
    User create(CreateUserCommand command);
}
