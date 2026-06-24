package in.elcot.avgcxr.platformnotificationclient.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * SMS delivery service for AVGC-XR Portal.
 * Integrates with the NIC SMS gateway used by Tamil Nadu government departments.
 *
 * <p>Supports bilingual SMS messages and OTP delivery. All SMS content must comply
 * with TRAI DND regulations and government communication guidelines.</p>
 */
@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    private final RestTemplate restTemplate;
    private final String gatewayUrl;
    private final String apiKey;
    private final String senderId;
    private final boolean enabled;

    public SmsService(
            @Value("${avgcxr.notification.sms.gateway-url:}") String gatewayUrl,
            @Value("${avgcxr.notification.sms.api-key:}") String apiKey,
            @Value("${avgcxr.notification.sms.sender-id:ELCOTN}") String senderId,
            @Value("${avgcxr.notification.sms.enabled:true}") boolean enabled) {
        this.restTemplate = new RestTemplate();
        this.gatewayUrl = gatewayUrl;
        this.apiKey = apiKey;
        this.senderId = senderId;
        this.enabled = enabled;
    }

    /**
     * Send an SMS message to a single recipient.
     *
     * @param mobileNumber recipient mobile number (10 digits, Indian)
     * @param message      SMS message content (max 160 characters for single SMS)
     * @param templateId   TRAI-approved template ID
     */
    public void sendSms(String mobileNumber, String message, String templateId) {
        if (!enabled) {
            log.info("SMS sending disabled. Would have sent to: {}", maskMobile(mobileNumber));
            return;
        }

        validateMobileNumber(mobileNumber);
        validateMessage(message);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-Key", apiKey);

            Map<String, String> body = Map.of(
                    "mobile", mobileNumber,
                    "message", message,
                    "senderId", senderId,
                    "templateId", templateId
            );

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(gatewayUrl, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("SMS sent successfully to: {}, templateId: {}", maskMobile(mobileNumber), templateId);
            } else {
                log.error("SMS delivery failed to: {}, status: {}, body: {}",
                        maskMobile(mobileNumber), response.getStatusCode(), response.getBody());
                throw new SmsDeliveryException("SMS gateway returned: " + response.getStatusCode());
            }
        } catch (SmsDeliveryException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to send SMS to: {}, error: {}", maskMobile(mobileNumber), e.getMessage());
            throw new SmsDeliveryException("Failed to send SMS: " + e.getMessage(), e);
        }
    }

    /**
     * Send an OTP via SMS.
     *
     * @param mobileNumber recipient mobile number
     * @param otp          the one-time password
     * @param validityMinutes OTP validity period in minutes
     */
    public void sendOtp(String mobileNumber, String otp, int validityMinutes) {
        String message = String.format("Your AVGC-XR Portal OTP is %s. Valid for %d minutes. "
                + "உங்கள் AVGC-XR தகவல் தளம் OTP %s. %d நிமிடங்களுக்கு செல்லுபடியாகும்.",
                otp, validityMinutes, otp, validityMinutes);
        sendSms(mobileNumber, message, "AVGC_OTP");
    }

    private void validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null || !mobileNumber.matches("^[6-9]\\d{9}$")) {
            throw new IllegalArgumentException("Invalid Indian mobile number: " + maskMobile(mobileNumber));
        }
    }

    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("SMS message must not be null or blank");
        }
        if (message.length() > 160) {
            log.warn("SMS message exceeds 160 characters ({} chars), will be split", message.length());
        }
    }

    private String maskMobile(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.length() < 4) {
            return "****";
        }
        return mobileNumber.substring(0, 2) + "******" + mobileNumber.substring(mobileNumber.length() - 2);
    }

    /**
     * Exception thrown when SMS delivery fails.
     */
    public static class SmsDeliveryException extends RuntimeException {
        public SmsDeliveryException(String message) {
            super(message);
        }

        public SmsDeliveryException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

/**
 * Immutable request object for SMS notifications.
 * (Definition in SmsRequest.java)
 */
