package in.elcot.avgcxr.platform.audit.application.port.input;

import in.elcot.avgcxr.platform.audit.api.rest.dto.response.AuditLogResponse;
import in.elcot.avgcxr.platform.audit.application.command.CreateAuditLogCommand;

public interface CreateAuditLogUseCase {
    AuditLogResponse create(CreateAuditLogCommand command);
}
