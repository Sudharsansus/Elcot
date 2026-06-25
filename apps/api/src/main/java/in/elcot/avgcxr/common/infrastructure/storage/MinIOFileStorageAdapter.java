package in.elcot.avgcxr.common.infrastructure.storage;

import in.elcot.avgcxr.platform.file.application.port.output.FileStoragePort;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * MinIO-backed implementation of FileStoragePort. Hexagonal boundary: application layer depends
 * only on the port.
 */
@Component
public class MinIOFileStorageAdapter implements FileStoragePort {

  private static final Logger log = LoggerFactory.getLogger(MinIOFileStorageAdapter.class);

  private final MinioClient minioClient;
  private final String bucket;

  public MinIOFileStorageAdapter(
      MinioClient minioClient, @Value("${minio.bucket:avgcxr-files}") String bucket) {
    this.minioClient = minioClient;
    this.bucket = bucket;
  }

  @Override
  public String upload(String objectName, byte[] content, String contentType) {
    try {
      minioClient.putObject(
          PutObjectArgs.builder().bucket(bucket).object(objectName).stream(
                  new ByteArrayInputStream(content), content.length, -1)
              .contentType(contentType == null ? "application/octet-stream" : contentType)
              .build());
      log.info(
          "Uploaded object {} to MinIO bucket {} ({} bytes)", objectName, bucket, content.length);
      return objectName;
    } catch (Exception e) {
      log.error("MinIO upload failed for object {}", objectName, e);
      throw new RuntimeException("File upload failed: " + e.getMessage(), e);
    }
  }
}
