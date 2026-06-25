package in.elcot.avgcxr.platform.search.application.port.input;

import in.elcot.avgcxr.platform.search.api.rest.dto.response.SearchResultResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetSearchResultUseCase {
  SearchResultResponse getById(UUID id);

  Page<SearchResultResponse> findAll(Pageable pageable);
}
