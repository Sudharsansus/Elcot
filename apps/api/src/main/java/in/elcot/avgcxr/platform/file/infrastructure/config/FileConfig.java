package in.elcot.avgcxr.platform.file.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * File service configuration.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>Max file size (default 25 MB)</li>
 *   <li>Allowed MIME types (PDF, images, video)</li>
 *   <li>MinIO bucket name and CDN base URL</li>
 * </ul>
 */
@Configuration
public class FileConfig {

    @Value("${avgcxr.file.max-size-bytes:26214400}")  // 25 MB
    private long maxSizeBytes;

    @Value("${avgcxr.file.allowed-mime-types:application/pdf,image/jpeg,image/png,image/webp,video/mp4,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document}")
    private String allowedMimeTypes;

    @Value("${avgcxr.file.bucket:avgcxr-files}")
    private String bucket;

    @Value("${avgcxr.file.cdn-base-url:}")
    private String cdnBaseUrl;

    @Bean
    public FileLimits fileLimits() {
        return new FileLimits(maxSizeBytes,
                java.util.Arrays.asList(allowedMimeTypes.split(",")).stream()
                        .map(String::trim).filter(s -> !s.isEmpty()).toList());
    }

    @Bean
    public FileStorageSettings fileStorageSettings() {
        return new FileStorageSettings(bucket, cdnBaseUrl);
    }

    public record FileLimits(long maxSizeBytes, java.util.List<String> allowedMimeTypes) {}

    public record FileStorageSettings(String bucket, String cdnBaseUrl) {}
}
