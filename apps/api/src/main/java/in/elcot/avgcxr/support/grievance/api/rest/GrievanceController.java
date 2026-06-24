package in.elcot.avgcxr.support.grievance.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.support.grievance.api.rest.dto.response.GrievanceResponse;
import in.elcot.avgcxr.support.grievance.application.port.input.GetGrievanceUseCase;
import in.elcot.avgcxr.support.grievance.application.port.input.CreateGrievanceUseCase;
import in.elcot.avgcxr.support.grievance.application.command.CreateGrievanceCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for DPDP Act 2023 compliant grievance redressal: 7-day acknowledgment, 30-day resolution, escalation.
 *
 * Provides CRUD operations and domain-specific actions
 * following the hexagonal architecture pattern.
 */
@RestController
@RequestMapping("/api/v1/grievances")
@RequiredArgsConstructor
public class GrievanceController {
    private final CreateGrievanceUseCase createGrievanceUseCase;
    private final GetGrievanceUseCase getGrievanceUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GrievanceResponse>>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<GrievanceResponse> results = getGrievanceUseCase.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.paginated(
                results.getContent(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GrievanceResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(getGrievanceUseCase.getById(id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<GrievanceResponse>> create(
            @Valid @RequestBody CreateGrievanceCommand command) {
        return ResponseEntity.ok(ApiResponse.success(createGrievanceUseCase.create(command)));
    }
}
