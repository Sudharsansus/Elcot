package in.elcot.avgcxr.policy.document.application.port.input;

import in.elcot.avgcxr.policy.document.application.command.ProcessDocumentCommand;
import in.elcot.avgcxr.policy.document.domain.model.DocumentId;

public interface ProcessDocumentUseCase {
  void process(DocumentId id, ProcessDocumentCommand command);
}
