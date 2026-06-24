package in.elcot.avgcxr.policy.document.infrastructure.persistence.repository;

import in.elcot.avgcxr.policy.document.infrastructure.persistence.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaDocumentRepository extends JpaRepository<DocumentEntity, UUID> {
}
