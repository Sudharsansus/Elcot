package in.elcot.avgcxr.platform.audit.domain.exception;

import java.util.UUID;

public class AuditNotFoundException extends RuntimeException {
  public AuditNotFoundException(UUID id) {
    super("Audit not found with id: " + id);
  }

  public AuditNotFoundException(String identifier) {
    super("Audit not found: " + identifier);
  }
}
