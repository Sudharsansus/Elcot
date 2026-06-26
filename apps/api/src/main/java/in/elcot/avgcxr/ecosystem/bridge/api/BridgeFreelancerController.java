package in.elcot.avgcxr.ecosystem.bridge.api;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.ecosystem.bridge.entity.FreelancerRegistryEntity;
import in.elcot.avgcxr.ecosystem.bridge.repository.FreelancerRegistryRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * AVGC-XR Bridge — Freelancer Registry directory (tender §IV). Public, paginated, filterable
 * listing of registered AVGC-XR freelancers; authenticated self-registration.
 */
@RestController
@RequestMapping("/api/v1/bridge/freelancers")
public class BridgeFreelancerController {

  private final FreelancerRegistryRepository repository;

  public BridgeFreelancerController(FreelancerRegistryRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<FreelancerResponse>>> list(
      @RequestParam(required = false) String district,
      @RequestParam(required = false) String specialization,
      @RequestParam(required = false) String q,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<FreelancerRegistryEntity> page =
        repository.search(
            nullIfBlank(district), nullIfBlank(specialization), nullIfBlank(q), pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            page.getContent().stream().map(BridgeFreelancerController::toResponse).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<FreelancerResponse>> getById(@PathVariable UUID id) {
    return repository
        .findById(id)
        .map(e -> ResponseEntity.ok(ApiResponse.success(toResponse(e))))
        .orElseGet(
            () ->
                ResponseEntity.status(404)
                    .body(ApiResponse.error("NOT_FOUND", "Freelancer not found: " + id)));
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<ApiResponse<FreelancerResponse>> register(
      @Valid @RequestBody FreelancerRequest req) {
    FreelancerRegistryEntity e = new FreelancerRegistryEntity();
    e.setFullName(req.fullName());
    e.setFullNameTamil(req.fullNameTamil());
    e.setEmail(req.email());
    e.setMobileNumber(req.mobileNumber());
    e.setSpecialization(req.specialization());
    e.setSkills(req.skills());
    e.setHourlyRate(req.hourlyRate());
    e.setAvailabilityStatus(req.availabilityStatus());
    e.setDistrict(req.district());
    FreelancerRegistryEntity saved = repository.save(e);
    return ResponseEntity.status(201)
        .body(ApiResponse.success(toResponse(saved), "Freelancer registered"));
  }

  private static String nullIfBlank(String s) {
    return StringUtils.hasText(s) ? s.trim() : null;
  }

  private static FreelancerResponse toResponse(FreelancerRegistryEntity e) {
    return new FreelancerResponse(
        e.getId(),
        e.getFullName(),
        e.getFullNameTamil(),
        e.getSpecialization(),
        e.getSkills(),
        e.getHourlyRate(),
        e.getAvailabilityStatus(),
        e.getDistrict(),
        e.getStatus());
  }

  public record FreelancerResponse(
      UUID id,
      String fullName,
      String fullNameTamil,
      String specialization,
      List<String> skills,
      BigDecimal hourlyRate,
      String availabilityStatus,
      String district,
      String status) {}

  public record FreelancerRequest(
      @NotBlank String fullName,
      String fullNameTamil,
      String email,
      String mobileNumber,
      String specialization,
      List<String> skills,
      BigDecimal hourlyRate,
      String availabilityStatus,
      String district) {}
}
