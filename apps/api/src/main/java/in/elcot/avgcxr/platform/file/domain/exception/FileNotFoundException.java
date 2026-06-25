package in.elcot.avgcxr.platform.file.domain.exception;

import java.util.UUID;

public class FileNotFoundException extends RuntimeException {
  public FileNotFoundException(UUID id) {
    super("File not found: " + id);
  }
}
