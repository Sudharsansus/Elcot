package in.elcot.avgcxr.platform.auth.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class DuplicateAuthException extends ConflictException {
  public DuplicateAuthException(String email) {
    super("DUPLICATE_EMAIL", "User already exists with email: " + email);
  }
}
