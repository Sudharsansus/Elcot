package in.elcot.avgcxr.platform.user.domain.exception;

public class DuplicateUserException extends RuntimeException {
  public DuplicateUserException(String field, String value) {
    super("User exists with " + field + ": " + value);
  }
}
