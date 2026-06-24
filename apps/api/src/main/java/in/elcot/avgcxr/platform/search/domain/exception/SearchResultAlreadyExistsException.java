package in.elcot.avgcxr.platform.search.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class SearchResultAlreadyExistsException extends ConflictException {
    public SearchResultAlreadyExistsException(String field, String value) {
        super("SEARCHRESULT_DUPLICATE", "SearchResult already exists with " + field + ": " + value);
    }
}
