package in.elcot.avgcxr.policy.application.api.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ApplicationResponse(
    UUID id,
    String applicationNumber,
    UUID schemeId,
    String schemeName,
    UUID applicantId,
    String applicantName,
    String status,
    String district,
    LocalDateTime submittedAt,
    LocalDateTime reviewedAt,
    BigDecimal fundingApproved,
    String rejectionReason) {}
