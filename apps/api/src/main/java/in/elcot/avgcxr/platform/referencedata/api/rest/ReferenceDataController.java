package in.elcot.avgcxr.platform.referencedata.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.repository.JpaDistrictRepository;
import in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.repository.JpaReferenceDataRepository;
import in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.repository.JpaSubspecialtyRepository;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public reference-data endpoints. Exposes Tamil Nadu's 38 districts, AVGC subspecialties, and any
 * custom reference data seeded in the {@code reference_data} table.
 *
 * <p>Closes Gap 2 of the audit: the {@code districts}, {@code subspecialties} and {@code
 * reference_data} tables now have JPA entities AND are read by this controller.
 */
@RestController
@RequestMapping("/api/v1/reference-data")
public class ReferenceDataController {

  private final JpaDistrictRepository districtRepo;
  private final JpaSubspecialtyRepository subspecialtyRepo;
  private final JpaReferenceDataRepository refDataRepo;

  public ReferenceDataController(
      JpaDistrictRepository districtRepo,
      JpaSubspecialtyRepository subspecialtyRepo,
      JpaReferenceDataRepository refDataRepo) {
    this.districtRepo = districtRepo;
    this.subspecialtyRepo = subspecialtyRepo;
    this.refDataRepo = refDataRepo;
  }

  @GetMapping("/districts")
  public ApiResponse<List<Map<String, String>>> districts() {
    List<Map<String, String>> list =
        districtRepo.findByOrderByNameEnAsc().stream()
            .map(
                d ->
                    Map.of(
                        "code", d.getCode(),
                        "nameEn", d.getNameEn(),
                        "nameTa", d.getNameTa()))
            .toList();
    return ApiResponse.success(list);
  }

  @GetMapping("/subspecialties")
  public ApiResponse<List<Map<String, String>>> subspecialties() {
    List<Map<String, String>> list =
        subspecialtyRepo.findByOrderByNameEnAsc().stream()
            .map(
                s ->
                    Map.of(
                        "code", s.getCode(),
                        "nameEn", s.getNameEn(),
                        "nameTa", s.getNameTa()))
            .toList();
    return ApiResponse.success(list);
  }

  @GetMapping("/{category}")
  public ApiResponse<List<Map<String, Object>>> byCategory(@PathVariable String category) {
    List<Map<String, Object>> list =
        refDataRepo.findByCategoryActive(category).stream()
            .map(
                r -> {
                  Map<String, Object> m = new java.util.LinkedHashMap<>();
                  m.put("id", r.getId());
                  m.put("category", r.getCategory());
                  m.put("code", r.getCode());
                  m.put("name", r.getName());
                  m.put("nameTamil", r.getNameTamil());
                  m.put("parentCode", r.getParentCode());
                  m.put("sortOrder", r.getSortOrder());
                  return m;
                })
            .toList();
    return ApiResponse.success(list);
  }
}
