package in.elcot.avgcxr.policy.application.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.policy.application.api.rest.dto.request.CreateApplicationRequest;
import in.elcot.avgcxr.policy.application.api.rest.dto.response.ApplicationResponse;
import in.elcot.avgcxr.policy.application.application.port.input.*;
import in.elcot.avgcxr.policy.application.application.command.CreateApplicationCommand;
import in.elcot.avgcxr.policy.application.application.command.ProcessApplicationCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final CreateApplicationUseCase createAppUseCase;
    private final GetApplicationUseCase getAppUseCase;
    private final ProcessApplicationUseCase processAppUseCase;

    /** Applicant: start a draft application */
    @PostMapping
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> createDraft(
            @Valid @RequestBody CreateApplicationRequest request) {
        ApplicationResponse app = createAppUseCase.createDraft(CreateApplicationCommand.from(request));
        return ResponseEntity.ok(ApiResponse.success(app, "Draft application created"));
    }

    /** Applicant: submit application */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> submit(@PathVariable UUID id) {
        ApplicationResponse app = processAppUseCase.submit(id);
        return ResponseEntity.ok(ApiResponse.success(app, "Application submitted"));
    }

    /** Applicant: view my applications */
    @GetMapping("/mine")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> myApplications(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "submittedAt") Pageable pageable) {
        Page<ApplicationResponse> apps = getAppUseCase.findByCurrentUser(status, pageable);
        return ResponseEntity.ok(ApiResponse.paginated(apps.getContent(), pageable.getPageNumber(),
                pageable.getPageSize(), apps.getTotalElements()));
    }

    /** Admin: list all applications */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER')")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> listAll(
            @RequestParam(required = false) String schemeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String district,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ApplicationResponse> apps = getAppUseCase.findAll(schemeId, status, district, pageable);
        return ResponseEntity.ok(ApiResponse.paginated(apps.getContent(), pageable.getPageNumber(),
                pageable.getPageSize(), apps.getTotalElements()));
    }

    /** Admin: approve application */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'NODAL_OFFICER')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> approve(
            @PathVariable UUID id, @RequestBody ProcessApplicationCommand.ApprovalRequest body) {
        ApplicationResponse app = processAppUseCase.approve(id, body.fundingApproved(), body.remarks());
        return ResponseEntity.ok(ApiResponse.success(app, "Application approved"));
    }

    /** Admin: reject application */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> reject(
            @PathVariable UUID id, @RequestBody ProcessApplicationCommand.RejectionRequest body) {
        ApplicationResponse app = processAppUseCase.reject(id, body.reason());
        return ResponseEntity.ok(ApiResponse.success(app, "Application rejected"));
    }
}
