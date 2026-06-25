package in.elcot.avgcxr.policy.document.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class DocumentAlreadyExistsException extends ConflictException {
  public DocumentAlreadyExistsException(String field, String value) {
    super("DOCUMENT_DUPLICATE", "Document already exists with " + field + ": " + value);
  }
}
