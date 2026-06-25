package in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.repository;

import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.entity.HelpdeskTicketEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaHelpdeskTicketRepository extends JpaRepository<HelpdeskTicketEntity, UUID> {}
