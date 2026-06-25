package in.elcot.avgcxr.policy.document.application.port.output;

import in.elcot.avgcxr.policy.document.domain.model.Document;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentRepositoryPort {
  Document save(Document entity);

  Optional<Document> findById(UUID id);

  Page<Document> findAll(Pageable pageable);

  void deleteById(UUID id);
}
