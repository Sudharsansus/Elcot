package in.elcot.avgcxr.policy.document.application.port.input;

import in.elcot.avgcxr.policy.document.api.rest.dto.response.DocumentResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetDocumentUseCase {
  DocumentResponse getById(UUID id);

  Page<DocumentResponse> findAll(Pageable pageable);
}
