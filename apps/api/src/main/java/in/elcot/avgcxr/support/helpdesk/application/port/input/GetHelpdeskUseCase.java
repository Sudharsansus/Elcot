package in.elcot.avgcxr.support.helpdesk.application.port.input;

import in.elcot.avgcxr.support.helpdesk.domain.model.Helpdesk;
import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskId;
import java.util.Optional;

public interface GetHelpdeskUseCase {
  Optional<Helpdesk> findById(HelpdeskId id);

  Helpdesk getById(HelpdeskId id);
}
