package in.elcot.avgcxr.platform.search.application.port.input;

import in.elcot.avgcxr.platform.search.domain.model.Search;
import in.elcot.avgcxr.platform.search.domain.model.SearchId;
import java.util.Optional;

public interface GetSearchUseCase {
    Optional<Search> findById(SearchId id);
    Search getById(SearchId id);
}
