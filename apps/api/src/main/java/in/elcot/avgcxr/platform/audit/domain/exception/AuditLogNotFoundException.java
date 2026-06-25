package in.elcot.avgcxr.platform.audit.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class AuditLogNotFoundException extends NotFoundException {
  public AuditLogNotFoundException(UUID id) {
    super("AUDITLOG_NOT_FOUND", "AuditLog not found with id: " + id);
  }
}
