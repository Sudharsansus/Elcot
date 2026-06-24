package in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.repository;

import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.entity.HelpdeskTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaHelpdeskTicketRepository extends JpaRepository<HelpdeskTicketEntity, UUID> {
}
