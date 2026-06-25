package in.elcot.avgcxr.support.helpdesk.application.port.output;

import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskTicket;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HelpdeskTicketRepositoryPort {
  HelpdeskTicket save(HelpdeskTicket entity);

  Optional<HelpdeskTicket> findById(UUID id);

  Page<HelpdeskTicket> findAll(Pageable pageable);

  void deleteById(UUID id);
}
