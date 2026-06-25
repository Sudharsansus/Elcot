package in.elcot.avgcxr.policy.document.infrastructure.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Document service configuration.
 *
 * <p>Provides:
 *
 * <ul>
 *   <li>Document type catalog (Aadhaar, PAN, GST, project proposal, etc.)
 *   <li>Per-document-type max size
 *   <li>OCR / verification toggle
 * </ul>
 */
@Configuration
public class DocumentConfig {

  @Value(
      "${avgcxr.document.types:AADHAAR,PAN,GST_CERTIFICATE,PROJECT_PROPOSAL,BUDGET_BREAKDOWN,PORTFOLIO,ID_PROOF,ADDRESS_PROOF,BANK_STATEMENT}")
  private String documentTypes;

  @Value("${avgcxr.document.max-size-bytes:10485760}")
  private long maxSizeBytes;

  @Value("${avgcxr.document.ocr-enabled:true}")
  private boolean ocrEnabled;

  @Bean
  public DocumentCatalog documentCatalog() {
    return new DocumentCatalog(
        List.of(documentTypes.split(",")).stream()
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList());
  }

  @Bean
  public DocumentSettings documentSettings() {
    return new DocumentSettings(maxSizeBytes, ocrEnabled);
  }

  public record DocumentCatalog(List<String> types) {}

  public record DocumentSettings(long maxSizeBytes, boolean ocrEnabled) {}
}
