package in.elcot.avgcxr.platform.audit.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.audit.infrastructure.persistence.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuditRepository extends JpaRepository<AuditEntity, String> {}
