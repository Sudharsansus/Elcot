package in.elcot.avgcxr.policy.document.domain.exception;

public class InvalidDocumentStateException extends RuntimeException {
  public InvalidDocumentStateException(String current, String target) {
    super("Cannot transition document from " + current + " to " + target);
  }
}
