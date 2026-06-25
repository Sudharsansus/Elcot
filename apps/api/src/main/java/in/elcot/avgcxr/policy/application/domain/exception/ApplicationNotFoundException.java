package in.elcot.avgcxr.policy.application.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class ApplicationNotFoundException extends NotFoundException {
  public ApplicationNotFoundException(UUID id) {
    super("APPLICATION_NOT_FOUND", "Application not found with id: " + id);
  }
}
