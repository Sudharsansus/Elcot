package in.elcot.avgcxr.platform.search.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.search.api.rest.dto.response.SearchResultResponse;
import in.elcot.avgcxr.platform.search.application.command.CreateSearchResultCommand;
import in.elcot.avgcxr.platform.search.application.port.input.CreateSearchResultUseCase;
import in.elcot.avgcxr.platform.search.application.port.input.GetSearchResultUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Elasticsearch-powered full-text search across schemes, applications,
 * companies, and talent with Tamil language support.
 *
 * <p>Provides CRUD operations and domain-specific actions following the hexagonal architecture
 * pattern.
 */
@RestController
@RequestMapping("/api/v1/search-results")
@RequiredArgsConstructor
public class SearchResultController {
  private final CreateSearchResultUseCase createSearchResultUseCase;
  private final GetSearchResultUseCase getSearchResultUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<SearchResultResponse>>> list(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<SearchResultResponse> results = getSearchResultUseCase.findAll(pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            results.getContent(),
            pageable.getPageNumber(),
            pageable.getPageSize(),
            results.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<SearchResultResponse>> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(getSearchResultUseCase.getById(id)));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ApiResponse<SearchResultResponse>> create(
      @Valid @RequestBody CreateSearchResultCommand command) {
    return ResponseEntity.ok(ApiResponse.success(createSearchResultUseCase.create(command)));
  }
}
