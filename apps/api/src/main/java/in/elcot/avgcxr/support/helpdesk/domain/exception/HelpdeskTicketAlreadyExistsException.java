package in.elcot.avgcxr.support.helpdesk.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class HelpdeskTicketAlreadyExistsException extends ConflictException {
    public HelpdeskTicketAlreadyExistsException(String field, String value) {
        super("HELPDESKTICKET_DUPLICATE", "HelpdeskTicket already exists with " + field + ": " + value);
    }
}
