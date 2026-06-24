package in.elcot.avgcxr.support.helpdesk.application.port.output;

import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface HelpdeskTicketRepositoryPort {
    HelpdeskTicket save(HelpdeskTicket entity);
    Optional<HelpdeskTicket> findById(UUID id);
    Page<HelpdeskTicket> findAll(Pageable pageable);
    void deleteById(UUID id);
}
