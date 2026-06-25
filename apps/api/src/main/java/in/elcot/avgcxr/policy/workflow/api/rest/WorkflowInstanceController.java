package in.elcot.avgcxr.policy.workflow.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.policy.workflow.api.rest.dto.response.WorkflowInstanceResponse;
import in.elcot.avgcxr.policy.workflow.application.command.CreateWorkflowInstanceCommand;
import in.elcot.avgcxr.policy.workflow.application.port.input.CreateWorkflowInstanceUseCase;
import in.elcot.avgcxr.policy.workflow.application.port.input.GetWorkflowInstanceUseCase;
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
 * REST controller for Flowable BPMN 2.0 workflow management for scheme approval processes with task
 * assignment and history.
 *
 * <p>Provides CRUD operations and domain-specific actions following the hexagonal architecture
 * pattern.
 */
@RestController
@RequestMapping("/api/v1/workflow-instances")
@RequiredArgsConstructor
public class WorkflowInstanceController {
  private final CreateWorkflowInstanceUseCase createWorkflowInstanceUseCase;
  private final GetWorkflowInstanceUseCase getWorkflowInstanceUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<WorkflowInstanceResponse>>> list(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<WorkflowInstanceResponse> results = getWorkflowInstanceUseCase.findAll(pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            results.getContent(),
            pageable.getPageNumber(),
            pageable.getPageSize(),
            results.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<WorkflowInstanceResponse>> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(getWorkflowInstanceUseCase.getById(id)));
  }

  @PreAuthorize("hasAnyRole('ADMIN','DISTRICT_OFFICER','NODAL_OFFICER')")
  @PostMapping
  public ResponseEntity<ApiResponse<WorkflowInstanceResponse>> create(
      @Valid @RequestBody CreateWorkflowInstanceCommand command) {
    return ResponseEntity.ok(ApiResponse.success(createWorkflowInstanceUseCase.create(command)));
  }
}
