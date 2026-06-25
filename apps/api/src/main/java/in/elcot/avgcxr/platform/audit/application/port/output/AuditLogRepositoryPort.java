package in.elcot.avgcxr.platform.audit.application.port.output;

import in.elcot.avgcxr.platform.audit.domain.model.AuditLog;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogRepositoryPort {
  AuditLog save(AuditLog entity);

  Optional<AuditLog> findById(UUID id);

  Page<AuditLog> findAll(Pageable pageable);

  void deleteById(UUID id);
}
