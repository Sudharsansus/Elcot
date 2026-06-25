package in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.response.FreelancerProfileResponse;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.command.CreateFreelancerProfileCommand;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input.CreateFreelancerProfileUseCase;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.port.input.GetFreelancerProfileUseCase;
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
 * REST controller for Freelancer registry with rate-based filtering, portfolio links, and
 * availability management.
 *
 * <p>Provides CRUD operations and domain-specific actions following the hexagonal architecture
 * pattern.
 */
@RestController
@RequestMapping("/api/v1/freelancer-profiles")
@RequiredArgsConstructor
public class FreelancerProfileController {
  private final CreateFreelancerProfileUseCase createFreelancerProfileUseCase;
  private final GetFreelancerProfileUseCase getFreelancerProfileUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<FreelancerProfileResponse>>> list(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<FreelancerProfileResponse> results = getFreelancerProfileUseCase.findAll(pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            results.getContent(),
            pageable.getPageNumber(),
            pageable.getPageSize(),
            results.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<FreelancerProfileResponse>> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(getFreelancerProfileUseCase.getById(id)));
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public ResponseEntity<ApiResponse<FreelancerProfileResponse>> create(
      @Valid @RequestBody CreateFreelancerProfileCommand command) {
    return ResponseEntity.ok(ApiResponse.success(createFreelancerProfileUseCase.create(command)));
  }
}
