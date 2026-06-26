package in.elcot.avgcxr.platform.notification.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.notification.infrastructure.delivery.EmailDeliveryProvider;
import in.elcot.avgcxr.platform.notification.infrastructure.delivery.SmsDeliveryProvider;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Internal notification delivery endpoints invoked by the worker's {@code NotificationListener}
 * after it consumes a dispatch event. Protected by a shared {@code X-Internal-Token} (never a JWT
 * user). The endpoints are permitAll in SecurityConfig but self-validate the token: if the token is
 * not configured the endpoints are closed (503), so they can never act as an open relay.
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationDeliveryController {

  private static final Logger log = LoggerFactory.getLogger(NotificationDeliveryController.class);

  private final EmailDeliveryProvider emailProvider;
  private final SmsDeliveryProvider smsProvider;
  private final String internalToken;

  public NotificationDeliveryController(
      EmailDeliveryProvider emailProvider,
      SmsDeliveryProvider smsProvider,
      @Value("${avgcxr.api.internal-token:}") String internalToken) {
    this.emailProvider = emailProvider;
    this.smsProvider = smsProvider;
    this.internalToken = internalToken;
  }

  @PostMapping("/email")
  public ResponseEntity<ApiResponse<String>> email(
      @RequestHeader(value = "X-Internal-Token", required = false) String token,
      @RequestBody Map<String, Object> payload) {
    ResponseEntity<ApiResponse<String>> denied = guard(token);
    if (denied != null) {
      return denied;
    }
    boolean ok =
        emailProvider.send(
            str(payload, "to"),
            str(payload, "subject", "Tamil Nadu AVGC-XR Portal"),
            str(payload, "body"));
    return ResponseEntity.ok(ApiResponse.success(ok ? "delivered" : "log-only"));
  }

  @PostMapping("/sms")
  public ResponseEntity<ApiResponse<String>> sms(
      @RequestHeader(value = "X-Internal-Token", required = false) String token,
      @RequestBody Map<String, Object> payload) {
    ResponseEntity<ApiResponse<String>> denied = guard(token);
    if (denied != null) {
      return denied;
    }
    String message = str(payload, "message");
    if (message.isBlank() && payload.get("otp") != null) {
      message = "Your Tamil Nadu AVGC-XR Portal OTP is " + payload.get("otp");
    }
    boolean ok = smsProvider.send(str(payload, "mobileNumber"), message);
    return ResponseEntity.ok(ApiResponse.success(ok ? "delivered" : "log-only"));
  }

  @PostMapping("/push")
  public ResponseEntity<ApiResponse<String>> push(
      @RequestHeader(value = "X-Internal-Token", required = false) String token,
      @RequestBody Map<String, Object> payload) {
    ResponseEntity<ApiResponse<String>> denied = guard(token);
    if (denied != null) {
      return denied;
    }
    // Push provider (FCM/WebPush) not yet integrated — accepted and logged.
    log.info("[push:log-only] payload keys={}", payload.keySet());
    return ResponseEntity.ok(ApiResponse.success("log-only"));
  }

  /** Returns a denial response, or {@code null} if the caller is authorized. */
  private ResponseEntity<ApiResponse<String>> guard(String token) {
    if (internalToken == null || internalToken.isBlank()) {
      return ResponseEntity.status(503)
          .body(ApiResponse.error("INTERNAL_TOKEN_UNSET", "Internal delivery is not configured"));
    }
    boolean match =
        token != null
            && MessageDigest.isEqual(
                token.getBytes(StandardCharsets.UTF_8),
                internalToken.getBytes(StandardCharsets.UTF_8));
    if (!match) {
      return ResponseEntity.status(403)
          .body(ApiResponse.error("FORBIDDEN", "Invalid internal token"));
    }
    return null;
  }

  private static String str(Map<String, Object> m, String key) {
    return str(m, key, "");
  }

  private static String str(Map<String, Object> m, String key, String dflt) {
    Object v = m.get(key);
    return v == null ? dflt : Objects.toString(v, dflt);
  }
}
