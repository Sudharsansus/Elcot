package in.elcot.avgcxr.support.helpdesk.application.port.input;

import in.elcot.avgcxr.support.helpdesk.application.command.CreateHelpdeskCommand;
import in.elcot.avgcxr.support.helpdesk.domain.model.Helpdesk;

public interface CreateHelpdeskUseCase {
  Helpdesk create(CreateHelpdeskCommand command);
}
