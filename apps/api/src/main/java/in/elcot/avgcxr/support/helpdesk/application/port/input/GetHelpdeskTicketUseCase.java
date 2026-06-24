package in.elcot.avgcxr.support.helpdesk.application.port.input;

import in.elcot.avgcxr.support.helpdesk.api.rest.dto.response.HelpdeskTicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetHelpdeskTicketUseCase {
    HelpdeskTicketResponse getById(UUID id);
    Page<HelpdeskTicketResponse> findAll(Pageable pageable);
}
