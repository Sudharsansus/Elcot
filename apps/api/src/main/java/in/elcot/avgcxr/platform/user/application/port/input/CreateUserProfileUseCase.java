package in.elcot.avgcxr.platform.user.application.port.input;

import in.elcot.avgcxr.platform.user.api.rest.dto.response.UserProfileResponse;
import in.elcot.avgcxr.platform.user.application.command.CreateUserProfileCommand;

public interface CreateUserProfileUseCase {
    UserProfileResponse create(CreateUserProfileCommand command);
}
