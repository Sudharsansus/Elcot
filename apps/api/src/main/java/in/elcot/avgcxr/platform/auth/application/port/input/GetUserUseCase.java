package in.elcot.avgcxr.platform.auth.application.port.input;

import in.elcot.avgcxr.platform.auth.api.rest.dto.response.AuthResponse;
import in.elcot.avgcxr.platform.auth.api.rest.dto.response.UserResponse;
import in.elcot.avgcxr.platform.auth.application.command.UpdateProfileCommand;

import java.util.UUID;

public interface GetUserUseCase {
    UserResponse getById(UUID id);
    UserResponse getCurrentUser();
    UUID getCurrentUserId();
    AuthResponse authenticate(String email, String password);
    AuthResponse refreshToken(String refreshToken);
    UserResponse update(UpdateProfileCommand command);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
}
