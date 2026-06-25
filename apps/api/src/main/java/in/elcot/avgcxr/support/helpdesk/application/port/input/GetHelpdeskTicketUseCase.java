package in.elcot.avgcxr.support.helpdesk.application.port.input;

import in.elcot.avgcxr.support.helpdesk.api.rest.dto.response.HelpdeskTicketResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetHelpdeskTicketUseCase {
  HelpdeskTicketResponse getById(UUID id);

  Page<HelpdeskTicketResponse> findAll(Pageable pageable);
}
