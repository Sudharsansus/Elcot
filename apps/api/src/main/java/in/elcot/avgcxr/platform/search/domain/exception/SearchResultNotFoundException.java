package in.elcot.avgcxr.platform.search.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class SearchResultNotFoundException extends NotFoundException {
  public SearchResultNotFoundException(UUID id) {
    super("SEARCHRESULT_NOT_FOUND", "SearchResult not found with id: " + id);
  }
}
