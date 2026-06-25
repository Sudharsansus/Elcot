package in.elcot.avgcxr.platform.search.api.rest;

import in.elcot.avgcxr.platform.search.api.rest.dto.request.CreateSearchRequest;
import in.elcot.avgcxr.platform.search.api.rest.dto.request.UpdateSearchRequest;
import in.elcot.avgcxr.platform.search.api.rest.dto.response.SearchResponse;
import in.elcot.avgcxr.platform.search.application.command.CreateSearchCommand;
import in.elcot.avgcxr.platform.search.application.command.UpdateSearchCommand;
import in.elcot.avgcxr.platform.search.application.service.SearchService;
import in.elcot.avgcxr.platform.search.domain.model.Search;
import in.elcot.avgcxr.platform.search.domain.model.SearchId;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/searchs")
public class SearchController {

  private final SearchService service;

  public SearchController(SearchService service) {
    this.service = service;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<SearchResponse> create(@Valid @RequestBody CreateSearchRequest request) {
    Search entity =
        service.create(new CreateSearchCommand(request.name(), request.description(), "system"));
    return ResponseEntity.created(URI.create("/api/v1/searchs/" + entity.getId()))
        .body(SearchResponse.from(entity));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SearchResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(SearchResponse.from(service.getById(SearchId.of(id))));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<SearchResponse> update(
      @PathVariable String id, @Valid @RequestBody UpdateSearchRequest request) {
    Search entity =
        service.update(
            SearchId.of(id), new UpdateSearchCommand(request.name(), request.description()));
    return ResponseEntity.ok(SearchResponse.from(entity));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(SearchId.of(id));
    return ResponseEntity.noContent().build();
  }
}
