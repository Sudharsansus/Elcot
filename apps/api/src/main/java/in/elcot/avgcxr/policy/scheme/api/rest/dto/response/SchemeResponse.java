package in.elcot.avgcxr.policy.scheme.api.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SchemeResponse(
    UUID id,
    String name,
    String nameTa,
    String description,
    String descriptionTa,
    String category,
    String subCategory,
    String ministry,
    String department,
    BigDecimal fundingAmountMin,
    BigDecimal fundingAmountMax,
    String fundingType,
    LocalDate applicationStartDate,
    LocalDate applicationEndDate,
    String status,
    boolean isActive,
    String thumbnailUrl,
    LocalDateTime createdAt
) {}
