package in.elcot.avgcxr.ecosystem.bridge.api;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.ecosystem.bridge.entity.BusinessConnectEntity;
import in.elcot.avgcxr.ecosystem.bridge.repository.BusinessConnectRepository;
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
 * AVGC-XR Bridge — Business Connect directory (tender §IV). Public, paginated, filterable listing
 * of registered AVGC-XR companies; authenticated self-registration.
 */
@RestController
@RequestMapping("/api/v1/bridge/companies")
public class BridgeBusinessController {

  private final BusinessConnectRepository repository;

  public BridgeBusinessController(BusinessConnectRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<BusinessResponse>>> list(
      @RequestParam(required = false) String district,
      @RequestParam(required = false) String subSector,
      @RequestParam(required = false) String q,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<BusinessConnectEntity> page =
        repository.search(nullIfBlank(district), nullIfBlank(subSector), nullIfBlank(q), pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            page.getContent().stream().map(BridgeBusinessController::toResponse).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<BusinessResponse>> getById(@PathVariable UUID id) {
    return repository
        .findById(id)
        .map(e -> ResponseEntity.ok(ApiResponse.success(toResponse(e))))
        .orElseGet(
            () ->
                ResponseEntity.status(404)
                    .body(ApiResponse.error("NOT_FOUND", "Company not found: " + id)));
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<ApiResponse<BusinessResponse>> register(
      @Valid @RequestBody BusinessRequest req) {
    BusinessConnectEntity e = new BusinessConnectEntity();
    e.setCompanyName(req.companyName());
    e.setCompanyNameTamil(req.companyNameTamil());
    e.setGstNumber(req.gstNumber());
    e.setRegistrationNumber(req.registrationNumber());
    e.setEntityType(req.entityType());
    e.setSubSector(req.subSector());
    e.setEmployeeCount(req.employeeCount());
    e.setTurnover(req.turnover());
    e.setDistrict(req.district());
    e.setAddress(req.address());
    e.setContactPerson(req.contactPerson());
    e.setContactEmail(req.contactEmail());
    e.setContactPhone(req.contactPhone());
    e.setWebsiteUrl(req.websiteUrl());
    BusinessConnectEntity saved = repository.save(e);
    return ResponseEntity.status(201)
        .body(ApiResponse.success(toResponse(saved), "Company registered"));
  }

  private static String nullIfBlank(String s) {
    return StringUtils.hasText(s) ? s.trim() : null;
  }

  private static BusinessResponse toResponse(BusinessConnectEntity e) {
    return new BusinessResponse(
        e.getId(),
        e.getCompanyName(),
        e.getCompanyNameTamil(),
        e.getEntityType(),
        e.getSubSector(),
        e.getEmployeeCount(),
        e.getTurnover(),
        e.getDistrict(),
        e.getContactPerson(),
        e.getContactEmail(),
        e.getContactPhone(),
        e.getWebsiteUrl(),
        e.getStatus());
  }

  public record BusinessResponse(
      UUID id,
      String companyName,
      String companyNameTamil,
      String entityType,
      String subSector,
      Integer employeeCount,
      BigDecimal turnover,
      String district,
      String contactPerson,
      String contactEmail,
      String contactPhone,
      String websiteUrl,
      String status) {}

  public record BusinessRequest(
      @NotBlank String companyName,
      String companyNameTamil,
      String gstNumber,
      String registrationNumber,
      String entityType,
      String subSector,
      Integer employeeCount,
      BigDecimal turnover,
      String district,
      String address,
      String contactPerson,
      String contactEmail,
      String contactPhone,
      String websiteUrl) {}
}
