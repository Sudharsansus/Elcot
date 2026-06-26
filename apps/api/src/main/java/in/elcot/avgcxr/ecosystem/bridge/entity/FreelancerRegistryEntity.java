package in.elcot.avgcxr.ecosystem.bridge.entity;

import in.elcot.avgcxr.common.infrastructure.security.EncryptedStringConverter;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * AVGC-XR Bridge — Freelancer Registry directory entry. Maps to the V6 {@code freelancer_registry}
 * table (previously no JPA entity). Personal contact PII (email, mobile) is encrypted at rest
 * (§IV.8).
 */
@Entity
@Table(name = "freelancer_registry")
@Getter
@Setter
public class FreelancerRegistryEntity {

  @Id
  @Column(name = "id", columnDefinition = "UUID")
  private UUID id;

  @Column(name = "full_name", nullable = false, length = 200)
  private String fullName;

  @Column(name = "full_name_tamil", length = 300)
  private String fullNameTamil;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "email", columnDefinition = "text")
  private String email;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "mobile_number", columnDefinition = "text")
  private String mobileNumber;

  @Column(name = "specialization", length = 100)
  private String specialization;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "skills", columnDefinition = "text[]")
  private List<String> skills;

  @Column(name = "hourly_rate")
  private BigDecimal hourlyRate;

  @Column(name = "availability_status", length = 30)
  private String availabilityStatus;

  @Column(name = "district", length = 100)
  private String district;

  @Column(name = "status", length = 30)
  private String status;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @PrePersist
  void onCreate() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    Instant now = Instant.now();
    createdAt = now;
    updatedAt = now;
    if (status == null) {
      status = "ACTIVE";
    }
    if (availabilityStatus == null) {
      availabilityStatus = "AVAILABLE";
    }
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = Instant.now();
  }
}
