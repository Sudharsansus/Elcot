package in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.adapter;

import in.elcot.avgcxr.support.helpdesk.application.port.output.HelpdeskTicketRepositoryPort;
import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskTicket;
import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.entity.HelpdeskTicketEntity;
import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.mapper.HelpdeskTicketMapper;
import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.repository.JpaHelpdeskTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HelpdeskTicketRepositoryAdapter implements HelpdeskTicketRepositoryPort {
    private final JpaHelpdeskTicketRepository jpaRepository;

    @Override
    public HelpdeskTicket save(HelpdeskTicket entity) {
        return HelpdeskTicketMapper.toDomain(jpaRepository.save(HelpdeskTicketMapper.toEntity(entity)));
    }

    @Override
    public Optional<HelpdeskTicket> findById(UUID id) {
        return jpaRepository.findById(id).map(HelpdeskTicketMapper::toDomain);
    }

    @Override
    public Page<HelpdeskTicket> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(HelpdeskTicketMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
