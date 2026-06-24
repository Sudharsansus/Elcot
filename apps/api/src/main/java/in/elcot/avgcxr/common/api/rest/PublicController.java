package in.elcot.avgcxr.common.api.rest;

import in.elcot.avgcxr.analytics.dashboard.application.port.output.DashboardRepositoryPort;
import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.repository.JpaDistrictRepository;
import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.repository.JpaSchemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Public-facing endpoints (no auth). Used by the public-portal frontend.
 * All figures are sourced from the live database, not hardcoded.
 */
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final DashboardRepositoryPort dashboardRepo;
    private final JpaSchemeRepository schemeRepo;
    private final JpaDistrictRepository districtRepo;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> stats() {
        Map<String, Long> byStatus = dashboardRepo.getApplicationsByStatus();
        long activeBeneficiaries =
                byStatus.getOrDefault("APPROVED", 0L) + byStatus.getOrDefault("DISBURSED", 0L);

        Map<String, Object> stats = Map.of(
                "totalSchemes", schemeRepo.countByStatus("PUBLISHED"),
                "activeBeneficiaries", activeBeneficiaries,
                "totalFundDisbursed", dashboardRepo.sumTotalDisbursed(),
                "districtsCovered", districtRepo.count()
        );
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/districts")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> districts() {
        List<Map<String, String>> districts = districtRepo.findByOrderByNameEnAsc().stream()
                .map(d -> Map.of(
                        "code", d.getCode(),
                        "nameEn", d.getNameEn(),
                        "nameTa", d.getNameTa()))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(districts));
    }

    @GetMapping("/subspecialties")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> subspecialties() {
        // AVGC-XR sub-sectors are stable reference data (seeded in V19, reference_data type SUB_SECTOR).
        List<Map<String, String>> subs = List.of(
                Map.of("code", "ANIMATION", "nameEn", "Animation", "nameTa", "அனிமேஷன்"),
                Map.of("code", "VFX", "nameEn", "Visual Effects", "nameTa", "விஷுவல் எஃபெக்ட்ஸ்"),
                Map.of("code", "GAMING", "nameEn", "Gaming", "nameTa", "கேமிங்"),
                Map.of("code", "COMICS", "nameEn", "Comics", "nameTa", "காமிக்ஸ்"),
                Map.of("code", "XR", "nameEn", "Extended Reality", "nameTa", "விரிவாக்கப்பட்ட யதார்த்தம்")
        );
        return ResponseEntity.ok(ApiResponse.success(subs));
    }
}
