package in.elcot.avgcxr.policy.application.domain.exception;

import in.elcot.avgcxr.platformcore.error.DomainException;
public class InvalidApplicationStateException extends DomainException {
    public InvalidApplicationStateException(String message) {
        super("INVALID_STATE", message);
    }
}
