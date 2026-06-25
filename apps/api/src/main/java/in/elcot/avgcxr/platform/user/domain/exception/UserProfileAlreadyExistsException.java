package in.elcot.avgcxr.platform.user.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class UserProfileAlreadyExistsException extends ConflictException {
  public UserProfileAlreadyExistsException(String field, String value) {
    super("USERPROFILE_DUPLICATE", "UserProfile already exists with " + field + ": " + value);
  }
}
