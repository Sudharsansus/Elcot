package in.elcot.avgcxr.policy.scheme.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.policy.scheme.api.rest.dto.request.CreateSchemeRequest;
import in.elcot.avgcxr.policy.scheme.api.rest.dto.response.SchemeResponse;
import in.elcot.avgcxr.policy.scheme.application.port.input.CreateSchemeUseCase;
import in.elcot.avgcxr.policy.scheme.application.port.input.GetSchemeUseCase;
import in.elcot.avgcxr.policy.scheme.application.command.CreateSchemeCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
    name = "Schemes",
    description = "AVGC-XR scheme catalogue and lifecycle — list/view (public) and create/publish/close (admin)")
@RestController
@RequestMapping("/api/v1/schemes")
@RequiredArgsConstructor
public class SchemeController {

    private final CreateSchemeUseCase createSchemeUseCase;
    private final GetSchemeUseCase getSchemeUseCase;

    /** Public: list all published schemes with filters */
    @Operation(
        summary = "List schemes",
        description =
            "Returns a paginated list of schemes, optionally filtered by category, status,"
                + " district or free-text search. Public endpoint.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SchemeResponse>>> listSchemes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "applicationEndDate") Pageable pageable) {
        Page<SchemeResponse> schemes = getSchemeUseCase.findAll(category, status, search, pageable);
        return ResponseEntity.ok(ApiResponse.paginated(schemes.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), schemes.getTotalElements()));
    }

    /** Public: get scheme by ID */
    @Operation(summary = "Get scheme by ID", description = "Returns a single scheme by its UUID. Public endpoint.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SchemeResponse>> getScheme(@PathVariable UUID id) {
        SchemeResponse scheme = getSchemeUseCase.getById(id);
        return ResponseEntity.ok(ApiResponse.success(scheme));
    }

    /** Admin: create a new scheme */
    @Operation(
        summary = "Create scheme",
        description = "Creates a new scheme in DRAFT status. Requires ROLE_ADMIN.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SchemeResponse>> createScheme(
            @Valid @RequestBody CreateSchemeRequest request) {
        SchemeResponse scheme = createSchemeUseCase.create(CreateSchemeCommand.from(request));
        return ResponseEntity.ok(ApiResponse.success(scheme, "Scheme created"));
    }

    /** Admin: publish a draft scheme */
    @Operation(summary = "Publish scheme", description = "Transitions a DRAFT scheme to PUBLISHED. Requires ROLE_ADMIN.")
    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SchemeResponse>> publishScheme(@PathVariable UUID id) {
        SchemeResponse scheme = createSchemeUseCase.updateStatus(id, "PUBLISHED");
        return ResponseEntity.ok(ApiResponse.success(scheme, "Scheme published"));
    }

    /** Admin: close a published scheme */
    @Operation(summary = "Close scheme", description = "Transitions a PUBLISHED scheme to CLOSED. Requires ROLE_ADMIN.")
    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SchemeResponse>> closeScheme(@PathVariable UUID id) {
        SchemeResponse scheme = createSchemeUseCase.updateStatus(id, "CLOSED");
        return ResponseEntity.ok(ApiResponse.success(scheme, "Scheme closed"));
    }
}
