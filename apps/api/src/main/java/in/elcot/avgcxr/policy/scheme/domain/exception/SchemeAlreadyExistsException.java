package in.elcot.avgcxr.policy.scheme.domain.exception;

public class SchemeAlreadyExistsException extends RuntimeException {
  public SchemeAlreadyExistsException(String name) {
    super("Scheme already exists: " + name);
  }
}
