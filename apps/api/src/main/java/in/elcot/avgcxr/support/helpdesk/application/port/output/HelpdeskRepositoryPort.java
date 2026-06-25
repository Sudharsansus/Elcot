package in.elcot.avgcxr.support.helpdesk.application.port.output;

import in.elcot.avgcxr.support.helpdesk.domain.model.Helpdesk;
import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskId;
import java.util.Optional;

/** Output port for Helpdesk persistence - implemented by infrastructure adapter */
public interface HelpdeskRepositoryPort {
  Helpdesk save(Helpdesk entity);

  Optional<Helpdesk> findById(HelpdeskId id);

  void deleteById(HelpdeskId id);

  boolean existsById(HelpdeskId id);
}
