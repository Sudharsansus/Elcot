package in.elcot.avgcxr.policy.scheme.domain.model;

import in.elcot.avgcxr.platformcore.model.AuditableEntity;
import in.elcot.avgcxr.policy.scheme.application.command.CreateSchemeCommand;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Pure domain model — no framework imports.
 * Persistence is handled by infrastructure.persistence.entity + mapper.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Scheme extends AuditableEntity {

 private UUID id;

 private String name;

 private String tamilName;

 private String description;

 private String descriptionTa;

 private SchemeCategory category;

 private String subCategory;

 private String ministry;

 private String department;

 private BigDecimal fundingAmountMin;

 private BigDecimal fundingAmountMax;

 private FundingType fundingType;

 private LocalDate applicationStartDate;

 private LocalDate applicationEndDate;

 @Builder.Default
 private SchemeStatus status = SchemeStatus.DRAFT;

 @Builder.Default
 private boolean active = true;

 private String thumbnailUrl;

 private LocalDateTime publishedAt;

 public enum SchemeCategory { ANIMATION, VFX, GAMING, COMICS, XR }
 public enum SchemeStatus { DRAFT, PUBLISHED, CLOSED, ARCHIVED }
 public enum FundingType { GRANT, SUBSIDY, LOAN, EQUITY, TAX_REBATE, COMBINED }

 public static Scheme create(CreateSchemeCommand cmd) {
 return Scheme.builder()
 .id(UUID.randomUUID())
 .name(cmd.name())
 .tamilName(cmd.nameTa())
 .description(cmd.description())
 .descriptionTa(cmd.descriptionTa())
 .category(SchemeCategory.valueOf(cmd.category()))
 .subCategory(cmd.subCategory())
 .ministry(cmd.ministry())
 .department(cmd.department())
 .fundingAmountMin(cmd.fundingAmountMin())
 .fundingAmountMax(cmd.fundingAmountMax())
 .fundingType(FundingType.valueOf(cmd.fundingType()))
 .applicationStartDate(cmd.applicationStartDate())
 .applicationEndDate(cmd.applicationEndDate())
 .status(SchemeStatus.DRAFT)
 .active(true)
 .build();
 }

 public void publish() {
 if (this.status != SchemeStatus.DRAFT) {
 throw new IllegalStateException("Only DRAFT schemes can be published");
 }
 this.status = SchemeStatus.PUBLISHED;
 this.active = true;
 this.publishedAt = LocalDateTime.now();
 }

 public void close() {
 if (this.status != SchemeStatus.PUBLISHED) {
 throw new IllegalStateException("Only PUBLISHED schemes can be closed");
 }
 this.status = SchemeStatus.CLOSED;
 }
}
