package in.elcot.avgcxr.analytics.reporting.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.analytics.reporting.api.rest.dto.response.ReportDataResponse;
import in.elcot.avgcxr.analytics.reporting.application.port.input.GetReportDataUseCase;
import in.elcot.avgcxr.analytics.reporting.application.port.input.CreateReportDataUseCase;
import in.elcot.avgcxr.analytics.reporting.application.command.CreateReportDataCommand;
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
 * REST controller for MIS report generation with CSV/PDF export: district-wise, scheme utilization, fund disbursement.
 *
 * Provides CRUD operations and domain-specific actions
 * following the hexagonal architecture pattern.
 */
@RestController
@RequestMapping("/api/v1/report-data")
@RequiredArgsConstructor
public class ReportDataController {
    private final CreateReportDataUseCase createReportDataUseCase;
    private final GetReportDataUseCase getReportDataUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportDataResponse>>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ReportDataResponse> results = getReportDataUseCase.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.paginated(
                results.getContent(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportDataResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(getReportDataUseCase.getById(id)));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DISTRICT_OFFICER','NODAL_OFFICER')")
    @PostMapping
    public ResponseEntity<ApiResponse<ReportDataResponse>> create(
            @Valid @RequestBody CreateReportDataCommand command) {
        return ResponseEntity.ok(ApiResponse.success(createReportDataUseCase.create(command)));
    }
}
