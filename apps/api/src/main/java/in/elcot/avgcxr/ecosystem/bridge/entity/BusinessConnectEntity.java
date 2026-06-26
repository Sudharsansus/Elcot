package in.elcot.avgcxr.ecosystem.bridge.entity;

import in.elcot.avgcxr.common.infrastructure.security.EncryptedStringConverter;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * AVGC-XR Bridge — Business Connect directory entry (tender §IV "AVGC-XR Bridge"). Maps to the V6
 * {@code business_connect} table, which previously had no JPA entity (the directory was
 * non-functional). Personal contact PII is encrypted at rest via {@link EncryptedStringConverter}
 * (§IV.8).
 */
@Entity
@Table(name = "business_connect")
@Getter
@Setter
public class BusinessConnectEntity {

  @Id
  @Column(name = "id", columnDefinition = "UUID")
  private UUID id;

  @Column(name = "company_name", nullable = false, length = 255)
  private String companyName;

  @Column(name = "company_name_tamil", length = 300)
  private String companyNameTamil;

  @Column(name = "gst_number", length = 20)
  private String gstNumber;

  @Column(name = "registration_number", length = 50)
  private String registrationNumber;

  @Column(name = "entity_type", length = 50)
  private String entityType;

  @Column(name = "sub_sector", length = 50)
  private String subSector;

  @Column(name = "employee_count")
  private Integer employeeCount;

  @Column(name = "turnover")
  private BigDecimal turnover;

  @Column(name = "district", length = 100)
  private String district;

  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  @Column(name = "contact_person", length = 200)
  private String contactPerson;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "contact_email", columnDefinition = "text")
  private String contactEmail;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "contact_phone", columnDefinition = "text")
  private String contactPhone;

  @Column(name = "website_url", length = 500)
  private String websiteUrl;

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
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = Instant.now();
  }
}
