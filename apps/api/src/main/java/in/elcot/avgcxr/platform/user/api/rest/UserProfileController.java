package in.elcot.avgcxr.platform.user.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.user.api.rest.dto.response.UserProfileResponse;
import in.elcot.avgcxr.platform.user.application.port.input.GetUserProfileUseCase;
import in.elcot.avgcxr.platform.user.application.port.input.CreateUserProfileUseCase;
import in.elcot.avgcxr.platform.user.application.command.CreateUserProfileCommand;
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
 * REST controller for Extended user profiles with skills, company details, and portfolio information.
 *
 * Provides CRUD operations and domain-specific actions
 * following the hexagonal architecture pattern.
 */
@RestController
@RequestMapping("/api/v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserProfileResponse> results = getUserProfileUseCase.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.paginated(
                results.getContent(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(getUserProfileUseCase.getById(id)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> create(
            @Valid @RequestBody CreateUserProfileCommand command) {
        return ResponseEntity.ok(ApiResponse.success(createUserProfileUseCase.create(command)));
    }
}
