package in.elcot.avgcxr.platform.user.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class UserProfileNotFoundException extends NotFoundException {
  public UserProfileNotFoundException(UUID id) {
    super("USERPROFILE_NOT_FOUND", "UserProfile not found with id: " + id);
  }
}
