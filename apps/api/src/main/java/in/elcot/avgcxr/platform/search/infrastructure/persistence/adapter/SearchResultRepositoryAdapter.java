package in.elcot.avgcxr.platform.search.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.search.application.port.output.SearchResultRepositoryPort;
import in.elcot.avgcxr.platform.search.domain.model.SearchResult;
import in.elcot.avgcxr.platform.search.infrastructure.persistence.mapper.SearchResultMapper;
import in.elcot.avgcxr.platform.search.infrastructure.persistence.repository.JpaSearchResultRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchResultRepositoryAdapter implements SearchResultRepositoryPort {
  private final JpaSearchResultRepository jpaRepository;

  @Override
  public SearchResult save(SearchResult entity) {
    return SearchResultMapper.toDomain(jpaRepository.save(SearchResultMapper.toEntity(entity)));
  }

  @Override
  public Optional<SearchResult> findById(UUID id) {
    return jpaRepository.findById(id).map(SearchResultMapper::toDomain);
  }

  @Override
  public Page<SearchResult> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(SearchResultMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
