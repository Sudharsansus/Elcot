package in.elcot.avgcxr.policy.scheme.domain.exception;

public class InvalidSchemeStateException extends RuntimeException {
  public InvalidSchemeStateException(String current, String target) {
    super("Cannot transition scheme from " + current + " to " + target);
  }
}
