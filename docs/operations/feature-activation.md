# Feature activation — env toggles

Every gateway/integration below ships **inert and safe**: with no env set, the feature logs-only or
reports `disabled`, and existing behaviour is unchanged. Set the vars (Render dashboard → service →
Environment, then re-sync the blueprint) to turn each on. Keys marked *secret* should never be
committed.

| Feature | Service | Env vars | Behaviour when unset |
|---|---|---|---|
| **PII encryption at rest** | api | `APP_ENCRYPTION_KEY`* (`openssl rand -base64 32`) | columns store plaintext |
| **Blind index (email/phone)** | api | `APP_BLIND_INDEX_KEY`* + cutover in [pii-blind-index.md](../security/pii-blind-index.md) | inert; plaintext lookups |
| **Email (SMTP)** | api | `AVGCXR_MAIL_ENABLED=true`, `AVGCXR_MAIL_FROM`, `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`, `SPRING_MAIL_USERNAME`*, `SPRING_MAIL_PASSWORD`* | logs only |
| **SMS (MSG91)** | api | `AVGCXR_SMS_MSG91_AUTH_KEY`*, `AVGCXR_SMS_MSG91_SENDER_ID` | logs only |
| **Internal delivery** | api + worker | `AVGCXR_API_INTERNAL_TOKEN`* (same on both); worker `AVGCXR_API_BASE_URL` | endpoints closed (503) |
| **Payment (Razorpay)** | api | `AVGCXR_PAYMENT_RAZORPAY_KEY_ID`, `AVGCXR_PAYMENT_RAZORPAY_KEY_SECRET`*, `AVGCXR_PAYMENT_CURRENCY` | `/orders` → 503, `config.enabled=false` |
| **SSO / OIDC** | api | `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KEYCLOAK_CLIENT_ID`, `..._CLIENT_SECRET`*, `SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KEYCLOAK_ISSUER_URI`, `AVGCXR_SSO_SUCCESS_REDIRECT` | SSO chain not created; JWT login only |
| **AI chatbot LLM** | api | `AVGCXR_LLM_PROVIDER=ollama`, `AVGCXR_LLM_OLLAMA_BASE_URL` | rule-based fallback |

## Notes

- **Email/SMS** flow: API publishes to RabbitMQ → worker `NotificationListener` POSTs back to the
  API's `/api/v1/notifications/{email,sms,push}` (token-protected) → the provider sends. Both the
  token (API + worker) and a real SMTP/MSG91 credential are needed for end-to-end delivery.
- **SSO**: standing up Keycloak as a stand-in ELCOT IdP — point `..._ISSUER_URI` at the realm and set
  the client id/secret; on login the API mints its own JWT (`OidcLoginSuccessHandler`) so RBAC is
  unchanged. For production, swap the issuer-uri for ELCOT's IdP.
- **Payment**: with Razorpay **test** keys this is a working sandbox; card/UPI data never touches the
  server (browser checkout → gateway), we only create orders and verify the signed callback.
- **Bridge demo data**: `V35` seeds clearly-labelled `(Demo)` directory rows; remove them before
  production via the cleanup block at the end of that migration.
