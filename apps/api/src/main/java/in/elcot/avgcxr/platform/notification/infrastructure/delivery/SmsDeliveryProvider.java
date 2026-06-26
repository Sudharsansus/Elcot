package in.elcot.avgcxr.platform.notification.infrastructure.delivery;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Real SMS delivery via MSG91 (tender gateway requirement; MSG91 is a common India-resident DLT
 * provider). Env-gated: sending requires {@code avgcxr.sms.msg91.auth-key} and a sender id. When
 * unconfigured the provider logs only and never fails — safe to deploy inert.
 *
 * <p>Uses MSG91's transactional HTTP endpoint. The exact route/sender/template must be DLT-approved
 * before production; values are env-driven so no India-telecom compliance detail is hard-coded.
 */
@Component
public class SmsDeliveryProvider {

  private static final Logger log = LoggerFactory.getLogger(SmsDeliveryProvider.class);
  private static final String MSG91_URL = "https://api.msg91.com/api/sendhttp.php";

  private final RestTemplate restTemplate;
  private final String authKey;
  private final String senderId;
  private final String route;
  private final String country;

  public SmsDeliveryProvider(
      @Value("${avgcxr.sms.msg91.auth-key:}") String authKey,
      @Value("${avgcxr.sms.msg91.sender-id:ELCOTN}") String senderId,
      @Value("${avgcxr.sms.msg91.route:4}") String route,
      @Value("${avgcxr.sms.msg91.country:91}") String country) {
    this.authKey = authKey;
    this.senderId = senderId;
    this.route = route;
    this.country = country;
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
    factory.setReadTimeout((int) Duration.ofSeconds(15).toMillis());
    this.restTemplate = new RestTemplate(factory);
  }

  public boolean send(String mobileNumber, String message) {
    if (authKey == null || authKey.isBlank()) {
      log.info("[sms:log-only] to={} (MSG91 not configured)", mask(mobileNumber));
      return false;
    }
    try {
      String url =
          UriComponentsBuilder.fromHttpUrl(MSG91_URL)
              .queryParam("authkey", authKey)
              .queryParam("mobiles", normalize(mobileNumber))
              .queryParam("message", message)
              .queryParam("sender", senderId)
              .queryParam("route", route)
              .queryParam("country", country)
              .toUriString();
      restTemplate.getForObject(url, String.class);
      log.info("SMS dispatched to {}", mask(mobileNumber));
      return true;
    } catch (Exception e) {
      log.error("SMS delivery failed to {}: {}", mask(mobileNumber), e.getMessage());
      return false;
    }
  }

  private static String normalize(String mobile) {
    return mobile == null ? "" : mobile.replaceAll("[^0-9]", "");
  }

  private static String mask(String mobile) {
    if (mobile == null || mobile.length() < 4) {
      return "****";
    }
    return "****" + mobile.substring(mobile.length() - 4);
  }
}
