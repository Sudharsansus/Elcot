package in.elcot.avgcxr.ecosystem.talentconnect.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.response.TalentProfileResponse;
import in.elcot.avgcxr.ecosystem.talentconnect.application.port.input.GetTalentProfileUseCase;
import in.elcot.avgcxr.ecosystem.talentconnect.application.port.input.CreateTalentProfileUseCase;
import in.elcot.avgcxr.ecosystem.talentconnect.application.command.CreateTalentProfileCommand;
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
 * REST controller for Talent professional registry with skill matching, experience filtering, and availability status.
 *
 * Provides CRUD operations and domain-specific actions
 * following the hexagonal architecture pattern.
 */
@RestController
@RequestMapping("/api/v1/talent-profiles")
@RequiredArgsConstructor
public class TalentProfileController {
    private final CreateTalentProfileUseCase createTalentProfileUseCase;
    private final GetTalentProfileUseCase getTalentProfileUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TalentProfileResponse>>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<TalentProfileResponse> results = getTalentProfileUseCase.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.paginated(
                results.getContent(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TalentProfileResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(getTalentProfileUseCase.getById(id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<TalentProfileResponse>> create(
            @Valid @RequestBody CreateTalentProfileCommand command) {
        return ResponseEntity.ok(ApiResponse.success(createTalentProfileUseCase.create(command)));
    }
}
