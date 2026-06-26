-- ─── V36: payment gateway records ──────────────────────────────────────────
-- Backs the payment module (tender required gateway). One row per order, tracking
-- its lifecycle CREATED → PAID/FAILED for audit/reconciliation. The actual card/UPI
-- data never touches this system — it stays with the PCI-DSS gateway (Razorpay).
CREATE TABLE IF NOT EXISTS payments (
    id             UUID PRIMARY KEY,
    provider       VARCHAR(30)  NOT NULL DEFAULT 'RAZORPAY',
    order_id       VARCHAR(100) NOT NULL,
    payment_id     VARCHAR(100),
    application_id UUID,
    payer          VARCHAR(255),
    amount_minor   BIGINT       NOT NULL,
    currency       VARCHAR(8)   NOT NULL DEFAULT 'INR',
    status         VARCHAR(20)  NOT NULL DEFAULT 'CREATED',
    receipt        VARCHAR(120),
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_payments_order ON payments(order_id);
CREATE INDEX IF NOT EXISTS idx_payments_application ON payments(application_id);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);
