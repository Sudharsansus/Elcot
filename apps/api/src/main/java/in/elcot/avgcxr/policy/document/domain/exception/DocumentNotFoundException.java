package in.elcot.avgcxr.policy.document.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class DocumentNotFoundException extends NotFoundException {
  public DocumentNotFoundException(UUID id) {
    super("DOCUMENT_NOT_FOUND", "Document not found with id: " + id);
  }
}
