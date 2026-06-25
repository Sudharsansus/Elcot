package in.elcot.avgcxr.policy.document.infrastructure.persistence.repository;

import in.elcot.avgcxr.policy.document.infrastructure.persistence.entity.DocumentEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDocumentRepository extends JpaRepository<DocumentEntity, UUID> {}
