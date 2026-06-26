package in.elcot.avgcxr.common.infrastructure.security;

import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Deterministic blind index for encrypted lookup keys (email, phone). A blind index is a keyed
 * HMAC-SHA256 of the normalised value: it is stable for the same input (so equality lookups and
 * unique constraints still work) but does not reveal the plaintext, letting a column's value be
 * encrypted at rest while remaining queryable by exact match (tender §IV.8).
 *
 * <p>Keyed by {@code APP_BLIND_INDEX_KEY} (use a value distinct from {@code APP_ENCRYPTION_KEY}).
 * Inert when unset — {@link #enabled()} is false and {@link #compute} returns {@code null} — so the
 * current plaintext lookups are completely unaffected until a coordinated cutover (see
 * docs/security/pii-blind-index.md).
 */
@Component
public class BlindIndexService {

  private final byte[] key;

  public BlindIndexService(@Value("${app.blind-index.key:#{null}}") String configuredKey) {
    String k = configuredKey;
    if (k == null || k.isBlank()) {
      k = System.getenv("APP_BLIND_INDEX_KEY");
    }
    this.key = (k == null || k.isBlank()) ? null : k.trim().getBytes(StandardCharsets.UTF_8);
  }

  /** Whether a blind-index key is configured. */
  public boolean enabled() {
    return key != null;
  }

  /**
   * Returns the hex HMAC-SHA256 blind index of {@code value} (lower-cased, trimmed), or {@code
   * null} if the value is null/blank or no key is configured.
   */
  public String compute(String value) {
    if (key == null || value == null || value.isBlank()) {
      return null;
    }
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(key, "HmacSHA256"));
      byte[] digest = mac.doFinal(value.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder(digest.length * 2);
      for (byte b : digest) {
        hex.append(Character.forDigit((b >> 4) & 0xF, 16)).append(Character.forDigit(b & 0xF, 16));
      }
      return hex.toString();
    } catch (Exception e) {
      throw new IllegalStateException("Failed to compute blind index", e);
    }
  }
}
