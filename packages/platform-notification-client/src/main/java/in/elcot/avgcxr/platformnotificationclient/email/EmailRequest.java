package in.elcot.avgcxr.platformnotificationclient.email;

import java.util.Map;

/**
 * Immutable request object for email notifications.
 */
public record EmailRequest(
    String to,
    String templateName,
    String locale,
    Map<String, Object> templateVariables
) {
    public EmailRequest {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Email recipient (to) must not be null or blank");
        }
        if (templateName == null || templateName.isBlank()) {
            throw new IllegalArgumentException("Template name must not be null or blank");
        }
    }
}
