package in.elcot.avgcxr.policy.application.application.command;

import java.math.BigDecimal;

public record ProcessApplicationCommand(
    String action,
    String comment,
    String reason
) {
    public record ApprovalRequest(BigDecimal fundingApproved, String remarks) {}
    public record RejectionRequest(String reason) {}
}
