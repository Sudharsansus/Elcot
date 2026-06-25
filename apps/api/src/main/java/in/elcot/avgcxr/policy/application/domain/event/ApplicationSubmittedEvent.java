package in.elcot.avgcxr.policy.application.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ApplicationSubmittedEvent extends DomainEvent {
  public ApplicationSubmittedEvent(
      UUID applicationId, String appNumber, UUID schemeId, UUID applicantId) {
    super(
        UUID.randomUUID(),
        "APPLICATION_SUBMITTED",
        LocalDateTime.now(),
        applicationId.toString(),
        Map.of(
            "applicationId",
            applicationId.toString(),
            "applicationNumber",
            appNumber,
            "schemeId",
            schemeId.toString(),
            "applicantId",
            applicantId.toString()));
  }
}
