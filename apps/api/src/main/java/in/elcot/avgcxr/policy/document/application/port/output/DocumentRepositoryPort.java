package in.elcot.avgcxr.policy.document.application.port.output;

import in.elcot.avgcxr.policy.document.domain.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepositoryPort {
    Document save(Document entity);
    Optional<Document> findById(UUID id);
    Page<Document> findAll(Pageable pageable);
    void deleteById(UUID id);
}
