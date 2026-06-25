package in.elcot.avgcxr.platform.search.domain.exception;

import java.util.UUID;

public class SearchNotFoundException extends RuntimeException {
  public SearchNotFoundException(UUID id) {
    super("Search not found with id: " + id);
  }

  public SearchNotFoundException(String identifier) {
    super("Search not found: " + identifier);
  }
}
