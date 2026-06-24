package in.elcot.avgcxr.platform.auth.application.command;

import in.elcot.avgcxr.platform.auth.api.rest.dto.request.UpdateUserRequest;
import java.util.UUID;

/**
 * Command for the self-profile update flow (PUT /api/v1/auth/me).
 *
 * <p>Previously named UpdateUserCommand which collided with the
 * {@code platform.user.application.command.UpdateUserCommand} (used by
 * admin-style user updates). Renamed for clarity per audit fix MEDIUM-015.</p>
 */
public record UpdateProfileCommand(UUID userId, UpdateUserRequest request) {}