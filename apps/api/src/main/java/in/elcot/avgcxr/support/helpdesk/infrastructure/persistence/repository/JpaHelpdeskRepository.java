package in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.repository;

import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.entity.HelpdeskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaHelpdeskRepository extends JpaRepository<HelpdeskEntity, String> {
}
