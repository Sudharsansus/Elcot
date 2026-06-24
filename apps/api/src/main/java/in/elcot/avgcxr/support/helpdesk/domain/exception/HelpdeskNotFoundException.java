package in.elcot.avgcxr.support.helpdesk.domain.exception;

import java.util.UUID;

public class HelpdeskNotFoundException extends RuntimeException {
    public HelpdeskNotFoundException(UUID id) { super("Helpdesk not found with id: " + id); }
    public HelpdeskNotFoundException(String identifier) { super("Helpdesk not found: " + identifier); }
}
