package in.elcot.avgcxr.platform.audit.infrastructure.persistence.repository;

import in.elcot.avgcxr.platform.audit.infrastructure.persistence.entity.AuditLogEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {}
