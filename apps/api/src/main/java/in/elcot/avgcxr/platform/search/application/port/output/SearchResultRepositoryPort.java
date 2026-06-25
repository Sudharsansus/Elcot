package in.elcot.avgcxr.platform.search.application.port.output;

import in.elcot.avgcxr.platform.search.domain.model.SearchResult;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchResultRepositoryPort {
  SearchResult save(SearchResult entity);

  Optional<SearchResult> findById(UUID id);

  Page<SearchResult> findAll(Pageable pageable);

  void deleteById(UUID id);
}
