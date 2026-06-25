package in.elcot.avgcxr.policy.document.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.policy.document.api.rest.dto.response.DocumentResponse;
import in.elcot.avgcxr.policy.document.application.command.CreateDocumentCommand;
import in.elcot.avgcxr.policy.document.application.port.input.CreateDocumentUseCase;
import in.elcot.avgcxr.policy.document.application.port.input.GetDocumentUseCase;
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
 * REST controller for Application document management with upload verification, type validation,
 * and MinIO integration.
 *
 * <p>Provides CRUD operations and domain-specific actions following the hexagonal architecture
 * pattern.
 */
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
  private final CreateDocumentUseCase createDocumentUseCase;
  private final GetDocumentUseCase getDocumentUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<DocumentResponse>>> list(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<DocumentResponse> results = getDocumentUseCase.findAll(pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            results.getContent(),
            pageable.getPageNumber(),
            pageable.getPageSize(),
            results.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<DocumentResponse>> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(getDocumentUseCase.getById(id)));
  }

  @PreAuthorize("hasAnyRole('APPLICANT','ADMIN','NODAL_OFFICER')")
  @PostMapping
  public ResponseEntity<ApiResponse<DocumentResponse>> create(
      @Valid @RequestBody CreateDocumentCommand command) {
    return ResponseEntity.ok(ApiResponse.success(createDocumentUseCase.create(command)));
  }
}
