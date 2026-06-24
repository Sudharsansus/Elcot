package in.elcot.avgcxr.policy.application.application.port.input;

import in.elcot.avgcxr.policy.application.api.rest.dto.response.ApplicationResponse;
import in.elcot.avgcxr.policy.application.application.command.ProcessApplicationCommand;
import java.math.BigDecimal;
import java.util.UUID;

public interface ProcessApplicationUseCase {
    ApplicationResponse process(UUID id, ProcessApplicationCommand command);
    ApplicationResponse submit(UUID id);
    ApplicationResponse approve(UUID id, BigDecimal fundingApproved, String remarks);
    ApplicationResponse reject(UUID id, String reason);
}
