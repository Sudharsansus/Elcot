package in.elcot.avgcxr.platform.audit.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class AuditLogAlreadyExistsException extends ConflictException {
    public AuditLogAlreadyExistsException(String field, String value) {
        super("AUDITLOG_DUPLICATE", "AuditLog already exists with " + field + ": " + value);
    }
}
