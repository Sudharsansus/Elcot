package in.elcot.avgcxr.platform.audit.application.port.output;

import in.elcot.avgcxr.platform.audit.domain.model.Audit;
import in.elcot.avgcxr.platform.audit.domain.model.AuditId;
import java.util.Optional;

/** Output port for Audit persistence - implemented by infrastructure adapter */
public interface AuditRepositoryPort {
    Audit save(Audit entity);
    Optional<Audit> findById(AuditId id);
    void deleteById(AuditId id);
    boolean existsById(AuditId id);
}
