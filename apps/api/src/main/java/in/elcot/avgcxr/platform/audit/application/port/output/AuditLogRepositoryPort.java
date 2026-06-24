package in.elcot.avgcxr.platform.audit.application.port.output;

import in.elcot.avgcxr.platform.audit.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface AuditLogRepositoryPort {
    AuditLog save(AuditLog entity);
    Optional<AuditLog> findById(UUID id);
    Page<AuditLog> findAll(Pageable pageable);
    void deleteById(UUID id);
}
