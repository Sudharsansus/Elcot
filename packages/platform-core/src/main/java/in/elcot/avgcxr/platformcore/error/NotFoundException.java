package in.elcot.avgcxr.platformcore.error;

public class NotFoundException extends DomainException {

  public NotFoundException(String message) {
    super("NOT_FOUND", message);
  }

  public NotFoundException(String entityName, Object id) {
    super("NOT_FOUND", entityName + " not found with id: " + id);
  }
}
