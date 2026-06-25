package in.elcot.avgcxr.platform.search.application.service;

import in.elcot.avgcxr.platform.search.application.command.CreateSearchCommand;
import in.elcot.avgcxr.platform.search.application.command.UpdateSearchCommand;
import in.elcot.avgcxr.platform.search.application.port.input.CreateSearchUseCase;
import in.elcot.avgcxr.platform.search.application.port.input.GetSearchUseCase;
import in.elcot.avgcxr.platform.search.application.port.output.SearchRepositoryPort;
import in.elcot.avgcxr.platform.search.domain.exception.SearchNotFoundException;
import in.elcot.avgcxr.platform.search.domain.model.Search;
import in.elcot.avgcxr.platform.search.domain.model.SearchId;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SearchService implements CreateSearchUseCase, GetSearchUseCase {

  private final SearchRepositoryPort repository;

  public SearchService(SearchRepositoryPort repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Search create(CreateSearchCommand command) {
    Search entity = new Search(SearchId.generate(), command.name());
    return repository.save(entity);
  }

  @Override
  public Optional<Search> findById(SearchId id) {
    return repository.findById(id);
  }

  @Override
  public Search getById(SearchId id) {
    return repository.findById(id).orElseThrow(() -> new SearchNotFoundException(id.value()));
  }

  @Transactional
  public Search update(SearchId id, UpdateSearchCommand command) {
    Search entity = getById(id);
    entity.markUpdated();
    return repository.save(entity);
  }

  @Transactional
  public void delete(SearchId id) {
    repository.deleteById(id);
  }
}
