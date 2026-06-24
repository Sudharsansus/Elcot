package in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.adapter;

import in.elcot.avgcxr.analytics.reporting.application.port.output.ReportingRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportingRepositoryAdapter implements ReportingRepositoryPort {
    private final EntityManager em;
    public ReportingRepositoryAdapter(EntityManager em) { this.em = em; }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getApplicationStats(LocalDate from, LocalDate to, String district) {
        String sql = "SELECT a.application_number, s.name as scheme, u.full_name as applicant, a.district, a.status, a.submission_date, a.approved_amount FROM applications a JOIN schemes s ON a.scheme_id = s.id JOIN users u ON a.applicant_id = u.id WHERE a.submission_date BETWEEN :from AND :to";
        if (district != null && !district.isBlank()) sql += " AND a.district = :district";
        Query q = em.createNativeQuery(sql);
        q.setParameter("from", from); q.setParameter("to", to);
        if (district != null && !district.isBlank()) q.setParameter("district", district);
        List<Object[]> results = q.getResultList();
        List<Map<String, Object>> rows = new ArrayList<>();
        String[] keys = {"app_number","scheme","applicant","district","status","submitted","amount"};
        for (Object[] r : results) { Map<String, Object> m = new HashMap<>(); for (int i = 0; i < Math.min(r.length, keys.length); i++) m.put(keys[i], r[i]); rows.add(m); }
        return rows;
    }

    @Override
    public List<Map<String, Object>> getDisbursementStats(LocalDate from, LocalDate to) { return getApplicationStats(from, to, null); }

    @Override
    public Map<String, Object> getApplicationSummary(LocalDate from, LocalDate to) {
        var q = em.createNativeQuery("SELECT COUNT(*), SUM(approved_amount) FROM applications WHERE submission_date BETWEEN :from AND :to");
        q.setParameter("from", from); q.setParameter("to", to);
        Object[] r = (Object[]) q.getSingleResult();
        Map<String, Object> m = new HashMap<>(); m.put("totalApplications", r[0]); m.put("totalAmount", r[1] != null ? r[1] : 0);
        return m;
    }
}

