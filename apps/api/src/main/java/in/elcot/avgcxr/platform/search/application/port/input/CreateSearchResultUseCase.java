package in.elcot.avgcxr.platform.search.application.port.input;

import in.elcot.avgcxr.platform.search.api.rest.dto.response.SearchResultResponse;
import in.elcot.avgcxr.platform.search.application.command.CreateSearchResultCommand;

public interface CreateSearchResultUseCase {
    SearchResultResponse create(CreateSearchResultCommand command);
}
