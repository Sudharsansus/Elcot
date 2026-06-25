package in.elcot.avgcxr.platformnotificationclient.sms;

import java.util.Map;

/** Immutable request object for SMS notifications. */
public record SmsRequest(
    String mobileNumber, String templateId, Map<String, Object> templateVariables) {}
