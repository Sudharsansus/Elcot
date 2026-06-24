package in.elcot.avgcxr.platform.audit.application.port.input;

import in.elcot.avgcxr.platform.audit.application.command.CreateAuditCommand;
import in.elcot.avgcxr.platform.audit.domain.model.Audit;

public interface CreateAuditUseCase {
    Audit create(CreateAuditCommand command);
}
