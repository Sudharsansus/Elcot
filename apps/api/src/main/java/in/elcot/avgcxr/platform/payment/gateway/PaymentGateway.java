package in.elcot.avgcxr.platform.payment.gateway;

/**
 * Abstraction over a payment gateway (tender required gateway). One implementation today ({@link
 * RazorpayPaymentGateway}); the interface keeps the controller provider-agnostic so a different
 * PA-DSS / RBI-approved aggregator can be swapped in without touching call sites.
 */
public interface PaymentGateway {

  /** Whether the gateway is configured (credentials present). */
  boolean isEnabled();

  /** Publishable key id, safe to hand to the browser checkout. */
  String publishableKey();

  /** ISO currency code (default INR). */
  String currency();

  /** Create an order for the given amount (in the currency's smallest unit, e.g. paise). */
  OrderResult createOrder(long amountMinor, String receipt);

  /** Verify the gateway callback signature after the customer completes checkout. */
  boolean verify(String orderId, String paymentId, String signature);

  record OrderResult(String orderId, long amountMinor, String currency) {}
}
