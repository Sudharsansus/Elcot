package in.elcot.avgcxr.policy.scheme.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "schemes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SchemeEntity {
    @Id @Column(name = "id") private UUID id;
    @Column(name = "name", nullable = false, length = 300) private String name;
    @Column(name = "name_ta", length = 300) private String nameTa;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT") private String description;
    @Column(name = "description_ta", columnDefinition = "TEXT") private String descriptionTa;
    @Column(name = "category", nullable = false) private String category;
    @Column(name = "sub_category", length = 100) private String subCategory;
    @Column(name = "ministry", length = 200) private String ministry;
    @Column(name = "department", length = 200) private String department;
    @Column(name = "funding_amount_min", precision = 15, scale = 2) private BigDecimal fundingAmountMin;
    @Column(name = "funding_amount_max", precision = 15, scale = 2) private BigDecimal fundingAmountMax;
    @Column(name = "funding_type", length = 20) private String fundingType;
    @Column(name = "application_start_date", nullable = false) private LocalDate applicationStartDate;
    @Column(name = "application_end_date", nullable = false) private LocalDate applicationEndDate;
    @Column(name = "status", nullable = false) private String status;
    @Column(name = "is_active", nullable = false) private boolean active;
    @Column(name = "thumbnail_url") private String thumbnailUrl;
    @Column(name = "published_at") private LocalDateTime publishedAt;
    @Column(name = "created_by") private UUID createdBy;
    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
}
