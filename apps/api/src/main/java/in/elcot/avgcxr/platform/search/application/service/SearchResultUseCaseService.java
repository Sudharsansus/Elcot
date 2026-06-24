package in.elcot.avgcxr.platform.search.application.service;

import in.elcot.avgcxr.platform.search.domain.exception.SearchResultNotFoundException;

import in.elcot.avgcxr.platform.search.api.rest.dto.response.SearchResultResponse;
import in.elcot.avgcxr.platform.search.application.command.CreateSearchResultCommand;
import in.elcot.avgcxr.platform.search.application.port.input.CreateSearchResultUseCase;
import in.elcot.avgcxr.platform.search.application.port.input.GetSearchResultUseCase;
import in.elcot.avgcxr.platform.search.infrastructure.persistence.entity.SearchResultEntity;
import in.elcot.avgcxr.platform.search.infrastructure.persistence.repository.JpaSearchResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Real implementation: persists to PostgreSQL via JPA.
 * Replaces the Phase-1 stub with full CRUD.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SearchResultUseCaseService implements CreateSearchResultUseCase, GetSearchResultUseCase {

    private final JpaSearchResultRepository repo;

    @Override
    @Transactional
    public SearchResultResponse create(CreateSearchResultCommand command) {
        log.info("Creating searchresult: {}", command);
        SearchResultEntity e = new SearchResultEntity();
        e.setId(UUID.randomUUID());
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        SearchResultEntity saved = repo.save(e);
        return toResponse(saved);
    }

    @Override
    public SearchResultResponse getById(UUID id) {
        SearchResultEntity e = repo.findById(id)
                .orElseThrow(() -> new SearchResultNotFoundException(id));
        return toResponse(e);
    }

    @Override
    public Page<SearchResultResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(SearchResultUseCaseService::toResponse);
    }

    private static SearchResultResponse toResponse(SearchResultEntity e) {
        return new SearchResultResponse(e.getId(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
