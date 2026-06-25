package in.elcot.avgcxr.support.helpdesk.domain.exception;

public class DuplicateHelpdeskException extends RuntimeException {
  public DuplicateHelpdeskException(String field, String value) {
    super("Helpdesk already exists with " + field + ": " + value);
  }
}
