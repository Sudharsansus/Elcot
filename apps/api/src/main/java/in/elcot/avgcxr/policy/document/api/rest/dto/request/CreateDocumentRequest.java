package in.elcot.avgcxr.policy.document.api.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateDocumentRequest(
    @NotBlank String documentTypeId, @NotBlank String applicationId) {}
