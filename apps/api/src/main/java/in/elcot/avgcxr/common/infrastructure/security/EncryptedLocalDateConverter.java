package in.elcot.avgcxr.common.infrastructure.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;

/**
 * JPA attribute converter that encrypts {@link LocalDate} columns at rest (AES-256-GCM), for
 * sensitive date PII such as date of birth (tender §IV.8).
 *
 * <p>The date is serialised to its ISO-8601 string form and handed to {@link
 * EncryptedStringConverter}, so it inherits the same key resolution, {@code enc:v1:} ciphertext
 * tagging and migration-safe plaintext fallback. A pre-existing plaintext value (e.g. {@code
 * 1990-01-01} from a column that used to be {@code DATE}) is parsed directly; with no key
 * configured the value is stored as a plain ISO string.
 */
@Converter
public class EncryptedLocalDateConverter implements AttributeConverter<LocalDate, String> {

  private static final EncryptedStringConverter DELEGATE = new EncryptedStringConverter();

  @Override
  public String convertToDatabaseColumn(LocalDate attribute) {
    if (attribute == null) {
      return null;
    }
    return DELEGATE.convertToDatabaseColumn(attribute.toString());
  }

  @Override
  public LocalDate convertToEntityAttribute(String dbValue) {
    if (dbValue == null || dbValue.isBlank()) {
      return null;
    }
    String plain = DELEGATE.convertToEntityAttribute(dbValue);
    return LocalDate.parse(plain.trim());
  }
}
