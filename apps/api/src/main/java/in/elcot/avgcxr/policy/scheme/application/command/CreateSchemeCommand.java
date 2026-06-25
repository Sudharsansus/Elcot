package in.elcot.avgcxr.policy.scheme.application.command;

import in.elcot.avgcxr.policy.scheme.api.rest.dto.request.CreateSchemeRequest;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateSchemeCommand(
    String name,
    String nameTa,
    String description,
    String descriptionTa,
    String category,
    String subCategory,
    String ministry,
    String department,
    LocalDate applicationStartDate,
    LocalDate applicationEndDate,
    BigDecimal fundingAmountMin,
    BigDecimal fundingAmountMax,
    String fundingType) {
  public static CreateSchemeCommand from(CreateSchemeRequest req) {
    return new CreateSchemeCommand(
        req.name(),
        req.nameTa(),
        req.description(),
        req.descriptionTa(),
        req.category(),
        req.subCategory(),
        req.ministry(),
        req.department(),
        req.applicationStartDate(),
        req.applicationEndDate(),
        req.fundingAmountMin(),
        req.fundingAmountMax(),
        req.fundingType());
  }
}
