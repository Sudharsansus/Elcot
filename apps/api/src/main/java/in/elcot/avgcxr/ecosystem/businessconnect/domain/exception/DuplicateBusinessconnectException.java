package in.elcot.avgcxr.ecosystem.businessconnect.domain.exception;



public class DuplicateBusinessconnectException extends RuntimeException {
    public DuplicateBusinessconnectException(String field, String value) { super("Businessconnect already exists with " + field + ": " + value); }
}
