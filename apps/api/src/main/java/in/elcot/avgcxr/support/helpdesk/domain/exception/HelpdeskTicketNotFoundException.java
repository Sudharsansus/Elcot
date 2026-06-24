package in.elcot.avgcxr.support.helpdesk.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class HelpdeskTicketNotFoundException extends NotFoundException {
    public HelpdeskTicketNotFoundException(UUID id) {
        super("HELPDESKTICKET_NOT_FOUND", "HelpdeskTicket not found with id: " + id);
    }
}
