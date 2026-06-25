package in.elcot.avgcxr.policy.document.application.port.input;

import in.elcot.avgcxr.policy.document.api.rest.dto.response.DocumentResponse;
import in.elcot.avgcxr.policy.document.application.command.CreateDocumentCommand;

public interface CreateDocumentUseCase {
  DocumentResponse create(CreateDocumentCommand command);
}
