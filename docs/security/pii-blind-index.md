# PII at-rest encryption for lookup keys (email / phone) — blind-index cutover

**Status:** foundation shipped (`BlindIndexService`, `V37` columns); the cutover below is a
**coordinated operational step**, deliberately *not* auto-deployed, because mis-sequencing it can
lock every user out of login.

## Why this is different from the other encrypted fields

`address`, `pincode`, `date_of_birth`, chat content, `companies.*` and the Bridge contact fields are
encrypted with a **random-IV** converter (`EncryptedStringConverter`) — safe because nothing queries
them by value.

`users.email` and `users.phone` are different:

- they are **equality lookup keys** (`findByEmail`, `findByMobileNumber`) with unique constraints, and
- `email` / `full_name` / `phone` also back the **admin partial search** (`LOWER(u.email) LIKE …`,
  see `JpaAuthRepository.search`).

Random-IV encryption breaks equality lookups; *any* encryption breaks `LIKE`. So encrypting them
needs a **deterministic blind index** for exact-match lookup, and the partial search must drop the
encrypted columns (search by name token / exact email only).

## What is already in place (safe, inert)

- `BlindIndexService` — keyed HMAC-SHA256 (`APP_BLIND_INDEX_KEY`); `enabled()` is false and
  `compute()` returns `null` until the key is set, so **today nothing changes**.
- `V37` — nullable `users.email_bidx` / `users.phone_bidx` (+ indexes). Additive only.

## Cutover (run with deploy control, not a blind push)

1. **Generate a key** distinct from `APP_ENCRYPTION_KEY`: `openssl rand -base64 32` →
   set `APP_BLIND_INDEX_KEY` on the API service. (`BlindIndexService.enabled()` becomes true.)
2. **Backfill** the blind-index columns for existing rows (one-off admin job / SQL via the app, since
   the HMAC must be computed in-app where the key lives):
   `email_bidx = hmac(lower(email))`, `phone_bidx = hmac(phone)`. Verify counts match.
3. **Switch lookups** to prefer the blind index with a plaintext fallback (lockout-proof even if a row
   was missed): in the auth/user adapters,
   `enabled() ? findByEmailBidx(compute(email)).or(() -> findByEmail(email)) : findByEmail(email)`.
   Also set `email_bidx`/`phone_bidx` on every save (forward-fill).
4. **Encrypt the value columns** — add `@Convert(EncryptedStringConverter.class)` to
   `AuthUserEntity.email` / `UserEntity.email` (+ phone) and widen them to `text` (new migration).
   At this point the stored email/phone are ciphertext; lookups go via `*_bidx`.
5. **Fix the partial search** — remove `email` / `phone` from `JpaAuthRepository.search` (they can no
   longer be `LIKE`-matched); keep name search, and add an exact-email lookup via `email_bidx`.
6. **Verify** end-to-end on staging: register, login, admin search, password reset — *before* the
   production deploy.

## Rollback
Unset `APP_BLIND_INDEX_KEY` and revert steps 4–5; the plaintext fallback (step 3) keeps login working
throughout, so a partial state is never a lockout.
