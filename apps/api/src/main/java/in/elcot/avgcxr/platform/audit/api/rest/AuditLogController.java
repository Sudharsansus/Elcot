package in.elcot.avgcxr.platform.audit.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.audit.api.rest.dto.response.AuditLogResponse;
import in.elcot.avgcxr.platform.audit.application.command.CreateAuditLogCommand;
import in.elcot.avgcxr.platform.audit.application.port.input.CreateAuditLogUseCase;
import in.elcot.avgcxr.platform.audit.application.port.input.GetAuditLogUseCase;
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
 * REST controller for Immutable audit trail for all sensitive operations, queryable with filters
 * and export.
 *
 * <p>Provides CRUD operations and domain-specific actions following the hexagonal architecture
 * pattern.
 */
@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
  private final CreateAuditLogUseCase createAuditLogUseCase;
  private final GetAuditLogUseCase getAuditLogUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<AuditLogResponse>>> list(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<AuditLogResponse> results = getAuditLogUseCase.findAll(pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            results.getContent(),
            pageable.getPageNumber(),
            pageable.getPageSize(),
            results.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<AuditLogResponse>> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(getAuditLogUseCase.getById(id)));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ApiResponse<AuditLogResponse>> create(
      @Valid @RequestBody CreateAuditLogCommand command) {
    return ResponseEntity.ok(ApiResponse.success(createAuditLogUseCase.create(command)));
  }
}
