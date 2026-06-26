package in.elcot.avgcxr.platform.payment.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** A payment order and its lifecycle (CREATED → PAID/FAILED), recorded for audit/reconciliation. */
@Entity
@Table(name = "payments")
@Getter
@Setter
public class PaymentEntity {

  @Id
  @Column(name = "id", columnDefinition = "UUID")
  private UUID id;

  @Column(name = "provider", length = 30, nullable = false)
  private String provider;

  @Column(name = "order_id", length = 100, nullable = false)
  private String orderId;

  @Column(name = "payment_id", length = 100)
  private String paymentId;

  @Column(name = "application_id", columnDefinition = "UUID")
  private UUID applicationId;

  @Column(name = "payer", length = 255)
  private String payer;

  @Column(name = "amount_minor", nullable = false)
  private long amountMinor;

  @Column(name = "currency", length = 8, nullable = false)
  private String currency;

  @Column(name = "status", length = 20, nullable = false)
  private String status;

  @Column(name = "receipt", length = 120)
  private String receipt;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  void onCreate() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    Instant now = Instant.now();
    createdAt = now;
    updatedAt = now;
    if (provider == null) {
      provider = "RAZORPAY";
    }
    if (status == null) {
      status = "CREATED";
    }
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = Instant.now();
  }
}
