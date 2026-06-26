package in.elcot.avgcxr.ecosystem.bridge.api;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.ecosystem.bridge.entity.TalentConnectEntity;
import in.elcot.avgcxr.ecosystem.bridge.repository.TalentConnectRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
 * AVGC-XR Bridge — Talent Connect directory (tender §IV). Public, paginated, filterable listing of
 * registered AVGC-XR professionals; authenticated self-registration.
 */
@RestController
@RequestMapping("/api/v1/bridge/talents")
public class BridgeTalentController {

  private final TalentConnectRepository repository;

  public BridgeTalentController(TalentConnectRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<TalentResponse>>> list(
      @RequestParam(required = false) String district,
      @RequestParam(required = false) String subSector,
      @RequestParam(required = false) String q,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<TalentConnectEntity> page =
        repository.search(nullIfBlank(district), nullIfBlank(subSector), nullIfBlank(q), pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            page.getContent().stream().map(BridgeTalentController::toResponse).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<TalentResponse>> getById(@PathVariable UUID id) {
    return repository
        .findById(id)
        .map(e -> ResponseEntity.ok(ApiResponse.success(toResponse(e))))
        .orElseGet(
            () ->
                ResponseEntity.status(404)
                    .body(ApiResponse.error("NOT_FOUND", "Talent not found: " + id)));
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<ApiResponse<TalentResponse>> register(
      @Valid @RequestBody TalentRequest req) {
    TalentConnectEntity e = new TalentConnectEntity();
    e.setFullName(req.fullName());
    e.setFullNameTamil(req.fullNameTamil());
    e.setEmail(req.email());
    e.setMobileNumber(req.mobileNumber());
    e.setSkills(req.skills());
    e.setExperienceYears(req.experienceYears());
    e.setSubSector(req.subSector());
    e.setPortfolioUrl(req.portfolioUrl());
    e.setDistrict(req.district());
    TalentConnectEntity saved = repository.save(e);
    return ResponseEntity.status(201)
        .body(ApiResponse.success(toResponse(saved), "Talent registered"));
  }

  private static String nullIfBlank(String s) {
    return StringUtils.hasText(s) ? s.trim() : null;
  }

  private static TalentResponse toResponse(TalentConnectEntity e) {
    return new TalentResponse(
        e.getId(),
        e.getFullName(),
        e.getFullNameTamil(),
        e.getSkills(),
        e.getExperienceYears(),
        e.getSubSector(),
        e.getPortfolioUrl(),
        e.getDistrict(),
        e.getStatus());
  }

  public record TalentResponse(
      UUID id,
      String fullName,
      String fullNameTamil,
      List<String> skills,
      Integer experienceYears,
      String subSector,
      String portfolioUrl,
      String district,
      String status) {}

  public record TalentRequest(
      @NotBlank String fullName,
      String fullNameTamil,
      String email,
      String mobileNumber,
      List<String> skills,
      Integer experienceYears,
      String subSector,
      String portfolioUrl,
      String district) {}
}
