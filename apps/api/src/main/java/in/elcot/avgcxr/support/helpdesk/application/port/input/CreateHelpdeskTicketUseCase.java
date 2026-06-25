package in.elcot.avgcxr.support.helpdesk.application.port.input;

import in.elcot.avgcxr.support.helpdesk.api.rest.dto.response.HelpdeskTicketResponse;
import in.elcot.avgcxr.support.helpdesk.application.command.CreateHelpdeskTicketCommand;

public interface CreateHelpdeskTicketUseCase {
  HelpdeskTicketResponse create(CreateHelpdeskTicketCommand command);
}
