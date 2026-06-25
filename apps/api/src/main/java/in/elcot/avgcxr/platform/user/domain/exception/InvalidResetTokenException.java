package in.elcot.avgcxr.platform.user.domain.exception;

public class InvalidResetTokenException extends RuntimeException {
  public InvalidResetTokenException(String message) {
    super(message);
  }
}
