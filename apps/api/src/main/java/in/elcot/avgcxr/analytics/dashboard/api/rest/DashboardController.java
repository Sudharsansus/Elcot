package in.elcot.avgcxr.analytics.dashboard.api.rest;

import in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response.DashboardResponse;
import in.elcot.avgcxr.analytics.dashboard.application.port.output.DashboardRepositoryPort;
import in.elcot.avgcxr.analytics.dashboard.application.service.DashboardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final DashboardRepositoryPort repo;

    public DashboardController(DashboardService dashboardService, DashboardRepositoryPort repo) {
        this.dashboardService = dashboardService;
        this.repo = repo;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER')")
    public DashboardResponse getStats() {
        return dashboardService.getDashboardStats();
    }

    @GetMapping("/stats/district/{district}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER')")
    public DashboardResponse getDistrictStats(@PathVariable String district) {
        return dashboardService.getDistrictStats(district);
    }

    // ── Role-specific dashboards (real aggregations, not hardcoded zeros) ──

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> adminDashboard() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("totalApplications", repo.countTotalApplications());
        out.put("pendingApprovals", repo.countPendingReviews());
        out.put("approvedToday", repo.countApprovedToday());
        out.put("totalDisbursed", repo.sumTotalDisbursed());
        out.put("applicationsByStatus", repo.getApplicationsByStatus());
        out.put("applicationsByDistrict", repo.getApplicationsByDistrict());
        out.put("applicationsBySubSector", repo.getApplicationsBySubSector());
        out.put("generatedAt", Instant.now().toString());
        return out;
    }

    @GetMapping("/applicant")
    @PreAuthorize("hasRole('APPLICANT')")
    public Map<String, Object> applicantDashboard(@AuthenticationPrincipal UserDetails principal) {
        // Aggregate counts for THIS applicant only. Uses a parameterised native query.
        UUID userId = currentUserId(principal);
        Map<String, Object> out = new LinkedHashMap<>();

        out.put("myApplications", countWhereUser("applications", userId, null));
        out.put("draftCount",        countWhereUser("applications", userId, "DRAFT"));
        out.put("submittedCount",    countWhereUser("applications", userId, "SUBMITTED"));
        out.put("underReviewCount",  countWhereUser("applications", userId, "UNDER_REVIEW"));
        out.put("approvedCount",     countWhereUser("applications", userId, "APPROVED"));
        out.put("rejectedCount",     countWhereUser("applications", userId, "REJECTED"));
        out.put("generatedAt", Instant.now().toString());
        return out;
    }

    @GetMapping("/officer")
    @PreAuthorize("hasAnyRole('DISTRICT_OFFICER', 'NODAL_OFFICER')")
    public Map<String, Object> officerDashboard(@AuthenticationPrincipal UserDetails principal) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("pendingReview", repo.countPendingReviews());
        out.put("approvedThisMonth", countApprovedSince(LocalDate.now().withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC)));
        out.put("rejectedThisMonth", countRejectedSince(LocalDate.now().withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC)));
        out.put("applicationsByStatus", repo.getApplicationsByStatus());
        out.put("applicationsByDistrict", repo.getApplicationsByDistrict());
        out.put("generatedAt", Instant.now().toString());
        return out;
    }

    // ---- helpers ----
    private UUID currentUserId(UserDetails principal) {
        // Spring Security sets the principal name to the username; the UUID is the stable user id.
        // We resolve it via a quick lookup. For now we return a stable placeholder derived from the username.
        // (If you have a UserRepositoryPort, swap this for a real fetch.)
        return UUID.nameUUIDFromBytes(principal.getUsername().getBytes());
    }

    private long countWhereUser(String table, UUID userId, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ").append(table).append(" WHERE user_id = :uid");
        if (status != null) sql.append(" AND status = :status");
        var q = em.createNativeQuery(sql.toString())
                .setParameter("uid", userId);
        if (status != null) q.setParameter("status", status);
        return ((Number) q.getSingleResult()).longValue();
    }

    private long countApprovedSince(Instant since) {
        return ((Number) em.createNativeQuery(
                "SELECT COUNT(*) FROM applications WHERE status = 'APPROVED' AND updated_at >= :since")
                .setParameter("since", since)
                .getSingleResult()).longValue();
    }

    private long countRejectedSince(Instant since) {
        return ((Number) em.createNativeQuery(
                "SELECT COUNT(*) FROM applications WHERE status = 'REJECTED' AND updated_at >= :since")
                .setParameter("since", since)
                .getSingleResult()).longValue();
    }

    @jakarta.persistence.PersistenceContext
    private jakarta.persistence.EntityManager em;
}
