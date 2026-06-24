package in.elcot.avgcxr.platformcore.error;

public class ConflictException extends DomainException {

    public ConflictException(String message) {
        super("CONFLICT", message);
    }

    public ConflictException(String code, String message) {
        super(code, message);
    }

    public ConflictException(String entityName, String field, Object value) {
        super("CONFLICT", entityName + " already exists with " + field + ": " + value);
    }
}