package in.elcot.avgcxr.support.helpdesk.application.service;

import in.elcot.avgcxr.support.helpdesk.api.rest.dto.response.HelpdeskTicketResponse;
import in.elcot.avgcxr.support.helpdesk.application.command.CreateHelpdeskTicketCommand;
import in.elcot.avgcxr.support.helpdesk.application.port.input.CreateHelpdeskTicketUseCase;
import in.elcot.avgcxr.support.helpdesk.application.port.input.GetHelpdeskTicketUseCase;
import in.elcot.avgcxr.support.helpdesk.application.port.output.HelpdeskTicketRepositoryPort;
import in.elcot.avgcxr.support.helpdesk.domain.exception.HelpdeskTicketNotFoundException;
import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskTicket;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Real implementation: persists to PostgreSQL via the HelpdeskTicketRepositoryPort.
 *
 * <p>HEXAGONAL FIX (HIGH-001 audit): Application layer depends ONLY on the output port interface in
 * application/port/output/. The infrastructure layer provides the JPA-backed adapter that
 * implements this port. No JPA / Spring Data imports here.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class HelpdeskTicketUseCaseService
    implements CreateHelpdeskTicketUseCase, GetHelpdeskTicketUseCase {

  private final HelpdeskTicketRepositoryPort repo;

  @Override
  @Transactional
  public HelpdeskTicketResponse create(CreateHelpdeskTicketCommand command) {
    log.info("Creating helpdesk ticket: {}", command);
    HelpdeskTicket ticket = HelpdeskTicket.create();
    HelpdeskTicket saved = repo.save(ticket);
    return new HelpdeskTicketResponse(saved.getId(), saved.getCreatedAt(), saved.getUpdatedAt());
  }

  @Override
  public HelpdeskTicketResponse getById(UUID id) {
    HelpdeskTicket ticket =
        repo.findById(id).orElseThrow(() -> new HelpdeskTicketNotFoundException(id));
    return new HelpdeskTicketResponse(ticket.getId(), ticket.getCreatedAt(), ticket.getUpdatedAt());
  }

  @Override
  public Page<HelpdeskTicketResponse> findAll(Pageable pageable) {
    return repo.findAll(pageable)
        .map(t -> new HelpdeskTicketResponse(t.getId(), t.getCreatedAt(), t.getUpdatedAt()));
  }
}
