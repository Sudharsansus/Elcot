package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.entity;

import in.elcot.avgcxr.common.infrastructure.security.EncryptedStringConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "name", length = 200)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "cin", columnDefinition = "TEXT")
  private String cin;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "gstin", columnDefinition = "TEXT")
  private String gstin;

  @Column(name = "district", length = 100)
  private String district;

  @Column(name = "state", length = 100)
  private String state;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "contact_email", columnDefinition = "TEXT")
  private String contactEmail;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "contact_phone", columnDefinition = "TEXT")
  private String contactPhone;

  @Column(name = "website", length = 500)
  private String website;

  @Column(name = "subsector", length = 50)
  private String subsector;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
