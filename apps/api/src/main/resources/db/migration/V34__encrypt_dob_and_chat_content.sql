-- ─── V34: widen date_of_birth for AES-256-GCM encryption at rest ───────────
-- EncryptedLocalDateConverter stores the ISO-8601 date as ciphertext
-- (`enc:v1:<base64>`), which no longer fits a native DATE column, so the
-- column is widened to text. Existing values are cast to their ISO string
-- form and remain readable (the converter treats unprefixed values as
-- legacy plaintext). Tender §IV.8: sensitive data in the DB must be
-- stored encrypted.
--
-- chat_messages.content / content_tamil are encrypted via @Convert too, but
-- those columns are already TEXT (see V28), so no DDL change is needed here.

ALTER TABLE user_profiles
    ALTER COLUMN date_of_birth TYPE text USING date_of_birth::text;
