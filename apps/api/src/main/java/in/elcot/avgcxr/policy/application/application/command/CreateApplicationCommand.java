package in.elcot.avgcxr.policy.application.application.command;

import in.elcot.avgcxr.policy.application.api.rest.dto.request.CreateApplicationRequest;
import java.util.UUID;

public record CreateApplicationCommand(
    UUID schemeId, UUID applicantId, String district, String formData) {
  public static CreateApplicationCommand from(CreateApplicationRequest req) {
    return new CreateApplicationCommand(
        req.schemeId(), req.applicantId(), req.district(), req.formData());
  }
}
