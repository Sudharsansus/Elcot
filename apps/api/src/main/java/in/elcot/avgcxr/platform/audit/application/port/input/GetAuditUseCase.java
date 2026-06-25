package in.elcot.avgcxr.platform.audit.application.port.input;

import in.elcot.avgcxr.platform.audit.domain.model.Audit;
import in.elcot.avgcxr.platform.audit.domain.model.AuditId;
import java.util.Optional;

public interface GetAuditUseCase {
  Optional<Audit> findById(AuditId id);

  Audit getById(AuditId id);
}
