package in.elcot.avgcxr.platform.search.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.search.application.port.output.SearchRepositoryPort;
import in.elcot.avgcxr.platform.search.domain.model.Search;
import in.elcot.avgcxr.platform.search.domain.model.SearchId;
import in.elcot.avgcxr.platform.search.infrastructure.persistence.mapper.SearchMapper;
import in.elcot.avgcxr.platform.search.infrastructure.persistence.repository.JpaSearchRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SearchRepositoryAdapter implements SearchRepositoryPort {

  private final JpaSearchRepository jpaRepo;
  private final SearchMapper mapper;

  public SearchRepositoryAdapter(JpaSearchRepository jpaRepo, SearchMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Search save(Search entity) {
    return mapper.toDomain(jpaRepo.save(mapper.toEntity(entity)));
  }

  @Override
  public Optional<Search> findById(SearchId id) {
    return jpaRepo.findById(id.toString()).map(mapper::toDomain);
  }

  @Override
  public void deleteById(SearchId id) {
    jpaRepo.deleteById(id.toString());
  }

  @Override
  public boolean existsById(SearchId id) {
    return jpaRepo.existsById(id.toString());
  }

  /**
   * RAG-friendly full-text search. Returns up to {@code limit} matching Search rows from the SQL
   * store (PostgreSQL ILIKE fallback when Elasticsearch is not configured).
   */
  @Override
  public List<Search> fullTextSearch(String query, int limit) {
    if (query == null || query.isBlank()) {
      return Collections.emptyList();
    }
    // PostgreSQL ILIKE on title and description; capped
    var entities = jpaRepo.fullTextSearch("%" + query + "%", Math.max(1, Math.min(20, limit)));
    return entities.stream().map(mapper::toDomain).toList();
  }
}
