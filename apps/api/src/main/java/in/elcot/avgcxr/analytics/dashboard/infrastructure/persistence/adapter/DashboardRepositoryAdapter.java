package in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.adapter;

import in.elcot.avgcxr.analytics.dashboard.application.port.output.DashboardRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class DashboardRepositoryAdapter implements DashboardRepositoryPort {
    private final EntityManager em;
    public DashboardRepositoryAdapter(EntityManager em) { this.em = em; }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Long> getApplicationsByStatus() {
        Query q = em.createNativeQuery("SELECT status, COUNT(*) FROM applications GROUP BY status");
        return toMap(q.getResultList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Long> getApplicationsByDistrict() {
        Query q = em.createNativeQuery("SELECT district, COUNT(*) FROM applications GROUP BY district");
        return toMap(q.getResultList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Long> getApplicationsBySubSector() {
        Query q = em.createNativeQuery("SELECT sub_sector, COUNT(*) FROM applications GROUP BY sub_sector");
        return toMap(q.getResultList());
    }

    @Override
    public long countTotalApplications() { return ((Number) em.createNativeQuery("SELECT COUNT(*) FROM applications").getSingleResult()).longValue(); }
    @Override
    public long countPendingReviews() { return ((Number) em.createNativeQuery("SELECT COUNT(*) FROM applications WHERE status = 'UNDER_REVIEW'").getSingleResult()).longValue(); }
    @Override
    public long countApprovedToday() { return ((Number) em.createNativeQuery("SELECT COUNT(*) FROM applications WHERE status = 'APPROVED' AND DATE(updated_at) = CURRENT_DATE").getSingleResult()).longValue(); }
    @Override
    public long sumTotalDisbursed() { return ((Number) em.createNativeQuery("SELECT COALESCE(SUM(approved_amount), 0) FROM applications WHERE status = 'DISBURSED'").getSingleResult()).longValue(); }

    private Map<String, Long> toMap(java.util.List<Object[]> results) {
        Map<String, Long> map = new HashMap<>();
        for (Object[] row : results) map.put(row[0].toString(), ((Number) row[1]).longValue());
        return map;
    }
}

