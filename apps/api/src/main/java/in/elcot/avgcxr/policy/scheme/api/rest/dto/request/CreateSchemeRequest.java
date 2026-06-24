package in.elcot.avgcxr.policy.scheme.api.rest.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateSchemeRequest(
    @NotBlank(message = "Scheme name is required")
    @Size(max = 300)
    String name,

    String nameTa,

    @NotBlank(message = "Description is required")
    String description,

    String descriptionTa,

    @NotBlank(message = "Category is required")
    String category,

    String subCategory,

    String ministry,

    @NotBlank(message = "Department is required")
    String department,

    @NotNull(message = "Application start date is required")
    @FutureOrPresent(message = "Start date must be today or later")
    LocalDate applicationStartDate,

    @NotNull(message = "Application end date is required")
    @Future(message = "End date must be in the future")
    LocalDate applicationEndDate,

    @DecimalMin(value = "0.00", message = "Minimum funding cannot be negative")
    BigDecimal fundingAmountMin,

    @DecimalMin(value = "0.00", message = "Maximum funding cannot be negative")
    BigDecimal fundingAmountMax,

    @NotBlank(message = "Funding type is required")
    String fundingType
) {}
