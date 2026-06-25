package in.elcot.avgcxr.platform.auth.application.command;

import in.elcot.avgcxr.platform.auth.api.rest.dto.request.CreateUserRequest;
import java.util.Map;

/**
 * Command for the user-registration flow (POST /api/v1/auth/register).
 *
 * <p>Previously named CreateUserCommand which collided with the {@code
 * platform.user.application.command.CreateUserCommand} (used by admin-style user creation in
 * /api/v1/users). Renamed for clarity per audit fix MEDIUM-014. Both records still exist with their
 * respective purposes; only the names differ.
 */
public record RegisterUserCommand(
    String email,
    String rawPassword,
    String fullName,
    String tamilName,
    String phone,
    String role,
    String district,
    Map<String, Boolean> consents,
    String ipAddress,
    String userAgent) {
  public static RegisterUserCommand from(CreateUserRequest request) {
    return new RegisterUserCommand(
        request.email(),
        request.password(),
        request.fullName(),
        request.tamilName(),
        request.phone(),
        request.role(),
        request.district(),
        null,
        null,
        null // consents + IP + UA populated at controller layer
        );
  }
}
