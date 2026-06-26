package in.elcot.avgcxr.common.infrastructure.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * JPA attribute converter that encrypts sensitive String columns at rest with AES-256-GCM,
 * satisfying the tender requirement (§IV.8) that sensitive information stored in the database be
 * kept in encrypted format.
 *
 * <p>The 256-bit key is read from {@code APP_ENCRYPTION_KEY} (base64 of 32 bytes; generate with
 * {@code openssl rand -base64 32}). The key is resolved from the environment rather than Spring so
 * it is available even when the JPA provider instantiates the converter directly.
 *
 * <p>Migration-safe: a value is only treated as ciphertext if it carries the {@code enc:v1:}
 * prefix, so pre-existing plaintext rows still read correctly and are re-encrypted on the next
 * write. If no key is configured (e.g. local dev), values are stored as-is.
 */
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

  private static final String ALGORITHM = "AES";
  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  private static final String PREFIX = "enc:v1:";
  private static final int IV_LENGTH = 12;
  private static final int TAG_LENGTH_BITS = 128;

  private static final SecureRandom RANDOM = new SecureRandom();
  private static volatile SecretKeySpec cachedKey;
  private static volatile boolean keyResolved;

  private static SecretKeySpec key() {
    if (!keyResolved) {
      synchronized (EncryptedStringConverter.class) {
        if (!keyResolved) {
          String b64 = System.getenv("APP_ENCRYPTION_KEY");
          if (b64 == null || b64.isBlank()) {
            b64 = System.getProperty("app.encryption.key");
          }
          if (b64 != null && !b64.isBlank()) {
            cachedKey = new SecretKeySpec(Base64.getDecoder().decode(b64.trim()), ALGORITHM);
          }
          keyResolved = true;
        }
      }
    }
    return cachedKey;
  }

  @Override
  public String convertToDatabaseColumn(String attribute) {
    if (attribute == null) {
      return null;
    }
    SecretKeySpec key = key();
    if (key == null) {
      return attribute; // not configured (dev) — store as-is
    }
    try {
      byte[] iv = new byte[IV_LENGTH];
      RANDOM.nextBytes(iv);
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
      byte[] ciphertext = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
      byte[] combined = new byte[iv.length + ciphertext.length];
      System.arraycopy(iv, 0, combined, 0, iv.length);
      System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
      return PREFIX + Base64.getEncoder().encodeToString(combined);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to encrypt sensitive field", e);
    }
  }

  @Override
  public String convertToEntityAttribute(String dbValue) {
    if (dbValue == null) {
      return null;
    }
    SecretKeySpec key = key();
    if (key == null || !dbValue.startsWith(PREFIX)) {
      return dbValue; // legacy plaintext or no key — return as-is
    }
    try {
      byte[] combined = Base64.getDecoder().decode(dbValue.substring(PREFIX.length()));
      byte[] iv = Arrays.copyOfRange(combined, 0, IV_LENGTH);
      byte[] ciphertext = Arrays.copyOfRange(combined, IV_LENGTH, combined.length);
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
      return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to decrypt sensitive field", e);
    }
  }
}
