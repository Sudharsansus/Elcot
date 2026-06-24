package in.elcot.avgcxr.platform.audit.domain.exception;



public class DuplicateAuditException extends RuntimeException {
    public DuplicateAuditException(String field, String value) { super("Audit already exists with " + field + ": " + value); }
}
