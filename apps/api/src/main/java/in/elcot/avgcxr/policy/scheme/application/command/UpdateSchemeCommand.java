package in.elcot.avgcxr.policy.scheme.application.command;

public record UpdateSchemeCommand(
    String name,
    String nameTamil,
    String description,
    String descriptionTamil,
    String category,
    String subSector,
    double maxSubsidyAmount,
    String startDate,
    String endDate) {}
