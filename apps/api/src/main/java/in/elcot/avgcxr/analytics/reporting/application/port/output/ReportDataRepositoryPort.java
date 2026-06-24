package in.elcot.avgcxr.analytics.reporting.application.port.output;

import in.elcot.avgcxr.analytics.reporting.domain.model.ReportData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for report data persistence. Implemented by
 * ReportDataRepositoryAdapter in infrastructure/persistence/.
 */
public interface ReportDataRepositoryPort {
    ReportData save(ReportData data);
    Optional<ReportData> findById(UUID id);
    Page<ReportData> findAll(Pageable pageable);
    void deleteById(UUID id);
}