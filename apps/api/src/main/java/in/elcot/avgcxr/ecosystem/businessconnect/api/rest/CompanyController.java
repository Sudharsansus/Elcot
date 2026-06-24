package in.elcot.avgcxr.ecosystem.businessconnect.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.response.CompanyResponse;
import in.elcot.avgcxr.ecosystem.businessconnect.application.port.input.GetCompanyUseCase;
import in.elcot.avgcxr.ecosystem.businessconnect.application.port.input.CreateCompanyUseCase;
import in.elcot.avgcxr.ecosystem.businessconnect.application.command.CreateCompanyCommand;
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
 * REST controller for AVGC-XR company directory with category filtering, skill-based search, and verification workflow.
 *
 * Provides CRUD operations and domain-specific actions
 * following the hexagonal architecture pattern.
 */
@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CreateCompanyUseCase createCompanyUseCase;
    private final GetCompanyUseCase getCompanyUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CompanyResponse> results = getCompanyUseCase.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.paginated(
                results.getContent(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(getCompanyUseCase.getById(id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> create(
            @Valid @RequestBody CreateCompanyCommand command) {
        return ResponseEntity.ok(ApiResponse.success(createCompanyUseCase.create(command)));
    }
}
