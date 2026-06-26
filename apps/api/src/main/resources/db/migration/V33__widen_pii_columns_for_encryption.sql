-- ─── V33: widen PII columns that are now AES-256-GCM encrypted at rest ──────
-- EncryptedStringConverter stores ciphertext as `enc:v1:<base64(iv+ciphertext+tag)>`,
-- which is longer than the plaintext, so the VARCHAR-limited columns must grow.
-- (user_profiles.address is already TEXT.) Tender §IV.8: sensitive data in the DB
-- must be stored encrypted.

ALTER TABLE user_profiles ALTER COLUMN pincode TYPE text;

ALTER TABLE companies ALTER COLUMN cin TYPE text;
ALTER TABLE companies ALTER COLUMN gstin TYPE text;
ALTER TABLE companies ALTER COLUMN contact_email TYPE text;
ALTER TABLE companies ALTER COLUMN contact_phone TYPE text;
