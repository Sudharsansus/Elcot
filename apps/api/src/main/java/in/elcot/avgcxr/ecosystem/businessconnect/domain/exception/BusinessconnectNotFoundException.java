package in.elcot.avgcxr.ecosystem.businessconnect.domain.exception;

import java.util.UUID;

public class BusinessconnectNotFoundException extends RuntimeException {
  public BusinessconnectNotFoundException(UUID id) {
    super("Businessconnect not found with id: " + id);
  }

  public BusinessconnectNotFoundException(String identifier) {
    super("Businessconnect not found: " + identifier);
  }
}
