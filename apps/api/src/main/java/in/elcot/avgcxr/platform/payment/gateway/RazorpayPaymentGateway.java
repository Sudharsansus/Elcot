package in.elcot.avgcxr.platform.payment.gateway;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Razorpay implementation of {@link PaymentGateway} using the REST API directly (no SDK
 * dependency). Env-gated by {@code avgcxr.payment.razorpay.key-id} / {@code key-secret}; with
 * Razorpay test keys this is a working sandbox integration, and it is fully inert (isEnabled=false)
 * when unset.
 *
 * <p>Order creation: {@code POST https://api.razorpay.com/v1/orders} with HTTP Basic auth.
 * Signature verification: {@code HMAC_SHA256(orderId + "|" + paymentId, key-secret)}.
 */
@Component
public class RazorpayPaymentGateway implements PaymentGateway {

  private static final Logger log = LoggerFactory.getLogger(RazorpayPaymentGateway.class);
  private static final String ORDERS_URL = "https://api.razorpay.com/v1/orders";

  private final RestTemplate restTemplate;
  private final String keyId;
  private final String keySecret;
  private final String currency;

  public RazorpayPaymentGateway(
      @Value("${avgcxr.payment.razorpay.key-id:}") String keyId,
      @Value("${avgcxr.payment.razorpay.key-secret:}") String keySecret,
      @Value("${avgcxr.payment.currency:INR}") String currency) {
    this.keyId = keyId;
    this.keySecret = keySecret;
    this.currency = currency;
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
    factory.setReadTimeout((int) Duration.ofSeconds(15).toMillis());
    this.restTemplate = new RestTemplate(factory);
  }

  @Override
  public boolean isEnabled() {
    return keyId != null && !keyId.isBlank() && keySecret != null && !keySecret.isBlank();
  }

  @Override
  public String publishableKey() {
    return keyId;
  }

  @Override
  public String currency() {
    return currency;
  }

  @Override
  @SuppressWarnings("unchecked")
  public OrderResult createOrder(long amountMinor, String receipt) {
    if (!isEnabled()) {
      throw new IllegalStateException("Payment gateway not configured");
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String basic =
        Base64.getEncoder()
            .encodeToString((keyId + ":" + keySecret).getBytes(StandardCharsets.UTF_8));
    headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basic);
    Map<String, Object> body =
        Map.of(
            "amount", amountMinor, "currency", currency, "receipt", receipt, "payment_capture", 1);
    Map<String, Object> resp =
        restTemplate
            .exchange(ORDERS_URL, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class)
            .getBody();
    if (resp == null || resp.get("id") == null) {
      throw new IllegalStateException("Razorpay order creation returned no id");
    }
    log.info("Razorpay order created: {} ({} {})", resp.get("id"), amountMinor, currency);
    return new OrderResult(resp.get("id").toString(), amountMinor, currency);
  }

  @Override
  public boolean verify(String orderId, String paymentId, String signature) {
    if (!isEnabled() || orderId == null || paymentId == null || signature == null) {
      return false;
    }
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      byte[] digest = mac.doFinal((orderId + "|" + paymentId).getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder(digest.length * 2);
      for (byte b : digest) {
        hex.append(Character.forDigit((b >> 4) & 0xF, 16)).append(Character.forDigit(b & 0xF, 16));
      }
      return java.security.MessageDigest.isEqual(
          hex.toString().getBytes(StandardCharsets.UTF_8),
          signature.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.error("Razorpay signature verification error: {}", e.getMessage());
      return false;
    }
  }
}
