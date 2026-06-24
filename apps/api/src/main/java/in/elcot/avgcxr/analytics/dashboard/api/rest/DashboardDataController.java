package in.elcot.avgcxr.analytics.dashboard.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response.DashboardDataResponse;
import in.elcot.avgcxr.analytics.dashboard.application.port.input.GetDashboardDataUseCase;
import in.elcot.avgcxr.analytics.dashboard.application.port.input.CreateDashboardDataUseCase;
import in.elcot.avgcxr.analytics.dashboard.application.command.CreateDashboardDataCommand;
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
 * REST controller for Real-time KPI aggregations: user counts, application status breakdowns, district distributions, trends.
 *
 * Provides CRUD operations and domain-specific actions
 * following the hexagonal architecture pattern.
 */
@RestController
@RequestMapping("/api/v1/analytics-dashboard")
@RequiredArgsConstructor
public class DashboardDataController {
    private final CreateDashboardDataUseCase createDashboardDataUseCase;
    private final GetDashboardDataUseCase getDashboardDataUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DashboardDataResponse>>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<DashboardDataResponse> results = getDashboardDataUseCase.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.paginated(
                results.getContent(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DashboardDataResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(getDashboardDataUseCase.getById(id)));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DISTRICT_OFFICER','NODAL_OFFICER')")
    @PostMapping
    public ResponseEntity<ApiResponse<DashboardDataResponse>> create(
            @Valid @RequestBody CreateDashboardDataCommand command) {
        return ResponseEntity.ok(ApiResponse.success(createDashboardDataUseCase.create(command)));
    }
}
