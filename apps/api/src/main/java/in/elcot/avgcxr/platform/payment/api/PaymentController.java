package in.elcot.avgcxr.platform.payment.api;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.payment.entity.PaymentEntity;
import in.elcot.avgcxr.platform.payment.gateway.PaymentGateway;
import in.elcot.avgcxr.platform.payment.repository.PaymentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Payment endpoints (tender required gateway). Card/UPI data never touches this server — the
 * browser checkout talks to the gateway directly; we only create orders and verify the signed
 * callback.
 *
 * <ul>
 *   <li>{@code GET /config} — public; lets the checkout init (publishable key only).
 *   <li>{@code POST /orders} — authenticated; create an order and persist it as CREATED.
 *   <li>{@code POST /verify} — authenticated; verify the gateway signature, mark PAID/FAILED.
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private final PaymentGateway gateway;
  private final PaymentRepository repository;

  public PaymentController(PaymentGateway gateway, PaymentRepository repository) {
    this.gateway = gateway;
    this.repository = repository;
  }

  @GetMapping("/config")
  public ResponseEntity<ApiResponse<PaymentConfig>> config() {
    return ResponseEntity.ok(
        ApiResponse.success(
            new PaymentConfig(
                gateway.isEnabled(),
                gateway.isEnabled() ? gateway.publishableKey() : null,
                gateway.currency())));
  }

  @PostMapping("/orders")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
      @Valid @RequestBody CreateOrderRequest req, Authentication auth) {
    if (!gateway.isEnabled()) {
      return ResponseEntity.status(503)
          .body(ApiResponse.error("PAYMENT_DISABLED", "Payment gateway is not configured"));
    }
    String receipt = req.receipt() != null ? req.receipt() : "rcpt_" + System.currentTimeMillis();
    PaymentGateway.OrderResult order = gateway.createOrder(req.amountMinor(), receipt);

    PaymentEntity e = new PaymentEntity();
    e.setProvider("RAZORPAY");
    e.setOrderId(order.orderId());
    e.setApplicationId(req.applicationId());
    e.setPayer(auth != null ? auth.getName() : null);
    e.setAmountMinor(order.amountMinor());
    e.setCurrency(order.currency());
    e.setReceipt(receipt);
    repository.save(e);

    return ResponseEntity.ok(
        ApiResponse.success(
            new OrderResponse(
                order.orderId(), gateway.publishableKey(), order.amountMinor(), order.currency())));
  }

  @PostMapping("/verify")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<ApiResponse<VerifyResponse>> verify(@Valid @RequestBody VerifyRequest req) {
    boolean ok = gateway.verify(req.orderId(), req.paymentId(), req.signature());
    PaymentEntity e = repository.findByOrderId(req.orderId()).orElse(null);
    if (e != null) {
      e.setPaymentId(req.paymentId());
      e.setStatus(ok ? "PAID" : "FAILED");
      repository.save(e);
    }
    String status = ok ? "PAID" : "FAILED";
    return ResponseEntity.ok(ApiResponse.success(new VerifyResponse(ok, status)));
  }

  public record PaymentConfig(boolean enabled, String keyId, String currency) {}

  public record CreateOrderRequest(
      @Positive long amountMinor, UUID applicationId, String receipt) {}

  public record OrderResponse(String orderId, String keyId, long amountMinor, String currency) {}

  public record VerifyRequest(
      @NotBlank String orderId, @NotBlank String paymentId, @NotBlank String signature) {}

  public record VerifyResponse(boolean verified, String status) {}
}
