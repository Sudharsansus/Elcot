package in.elcot.avgcxr.platform.auth.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class AuthNotFoundException extends NotFoundException {
  public AuthNotFoundException(UUID id) {
    super("AUTH_NOT_FOUND", "User not found with id: " + id);
  }

  public AuthNotFoundException(String email) {
    super("AUTH_NOT_FOUND", "User not found with email: " + email);
  }
}
