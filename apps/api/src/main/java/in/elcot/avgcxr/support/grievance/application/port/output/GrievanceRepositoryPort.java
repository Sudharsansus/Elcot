package in.elcot.avgcxr.support.grievance.application.port.output;

import in.elcot.avgcxr.support.grievance.domain.model.Grievance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface GrievanceRepositoryPort {
    Grievance save(Grievance entity);
    Optional<Grievance> findById(UUID id);
    Page<Grievance> findAll(Pageable pageable);
    void deleteById(UUID id);
}
