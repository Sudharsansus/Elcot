package in.elcot.avgcxr.support.helpdesk.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.support.helpdesk.api.rest.dto.response.HelpdeskTicketResponse;
import in.elcot.avgcxr.support.helpdesk.application.command.CreateHelpdeskTicketCommand;
import in.elcot.avgcxr.support.helpdesk.application.port.input.CreateHelpdeskTicketUseCase;
import in.elcot.avgcxr.support.helpdesk.application.port.input.GetHelpdeskTicketUseCase;
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
 * REST controller for Helpdesk ticket management with priority-based routing, SLA tracking, and
 * resolution workflow.
 *
 * <p>Provides CRUD operations and domain-specific actions following the hexagonal architecture
 * pattern.
 */
@RestController
@RequestMapping("/api/v1/helpdesk-tickets")
@RequiredArgsConstructor
public class HelpdeskTicketController {
  private final CreateHelpdeskTicketUseCase createHelpdeskTicketUseCase;
  private final GetHelpdeskTicketUseCase getHelpdeskTicketUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<HelpdeskTicketResponse>>> list(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<HelpdeskTicketResponse> results = getHelpdeskTicketUseCase.findAll(pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            results.getContent(),
            pageable.getPageNumber(),
            pageable.getPageSize(),
            results.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<HelpdeskTicketResponse>> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(getHelpdeskTicketUseCase.getById(id)));
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public ResponseEntity<ApiResponse<HelpdeskTicketResponse>> create(
      @Valid @RequestBody CreateHelpdeskTicketCommand command) {
    return ResponseEntity.ok(ApiResponse.success(createHelpdeskTicketUseCase.create(command)));
  }
}
