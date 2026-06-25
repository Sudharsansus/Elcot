package in.elcot.avgcxr.platform.file.application.port.output;

/**
 * Output port for binary file storage. Implemented by the MinIO-backed adapter in the
 * infrastructure layer. Application services depend only on this interface (hexagonal).
 */
public interface FileStoragePort {
  /**
   * Upload the byte content to a durable object store and return the storage key (object name) by
   * which the content can be retrieved.
   */
  String upload(String objectName, byte[] content, String contentType);
}
