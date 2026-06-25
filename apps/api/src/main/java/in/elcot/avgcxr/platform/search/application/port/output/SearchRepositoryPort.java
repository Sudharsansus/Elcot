package in.elcot.avgcxr.platform.search.application.port.output;

import in.elcot.avgcxr.platform.search.domain.model.Search;
import in.elcot.avgcxr.platform.search.domain.model.SearchId;
import java.util.List;
import java.util.Optional;

/**
 * Output port for Search persistence + retrieval. Implemented by infrastructure adapter that talks
 * to Elasticsearch.
 */
public interface SearchRepositoryPort {
  Search save(Search entity);

  Optional<Search> findById(SearchId id);

  void deleteById(SearchId id);

  boolean existsById(SearchId id);

  /**
   * RAG-friendly full-text search across scheme/company/talent/freelancer documents. Returns up to
   * {@code limit} matching documents.
   *
   * <p>If Elasticsearch is unavailable, implementations may return an empty list.
   */
  List<Search> fullTextSearch(String query, int limit);
}
