package in.elcot.avgcxr.platform.search.domain.exception;

public class DuplicateSearchException extends RuntimeException {
  public DuplicateSearchException(String field, String value) {
    super("Search already exists with " + field + ": " + value);
  }
}
