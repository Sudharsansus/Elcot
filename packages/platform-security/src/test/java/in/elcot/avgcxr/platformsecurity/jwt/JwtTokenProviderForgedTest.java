package in.elcot.avgcxr.platformsecurity.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regression test for the JWT validation fix (audit M-2).
 *
 * <p>Before the fix, {@code validateToken} caught {@code java.lang.SecurityException}
 * instead of {@code io.jsonwebtoken.JwtException}, so a tampered/forged token threw
 * {@code io.jsonwebtoken.security.SignatureException} out of the method (HTTP 500)
 * rather than being rejected. These tests assert clean rejection (return false),
 * and that genuinely valid tokens still pass.</p>
 */
class JwtTokenProviderForgedTest {

    // Base64 string that decodes to 51 bytes (>= 32 required for HS256).
    private static final String SECRET =
            "YXZnY3hyLXRlc3Qtc2VjcmV0LWtleS1mb3ItaHMyNTYtc2lnbmluZy0wMTIzNDU2Nzg5";

    private JwtTokenProvider provider;

    @BeforeEach
    void setup() {
        // (secret, accessTokenExpirationMs, refreshTokenExpirationMs)
        provider = new JwtTokenProvider(SECRET, 3_600_000L, 86_400_000L);
    }

    @Test
    void rejectsForgedToken() {
        // Valid header + payload but a bogus signature -> SignatureException internally.
        String forged = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYWNrZXIifQ.bogus_signature_xxxxxxxxxxxxxxxxxx";
        assertThat(provider.validateToken(forged)).isFalse();
    }

    @Test
    void rejectsMalformedToken() {
        assertThat(provider.validateToken("not.a.jwt")).isFalse();
    }

    @Test
    void rejectsNullAndEmpty() {
        assertThat(provider.validateToken(null)).isFalse();
        assertThat(provider.validateToken("")).isFalse();
    }

    @Test
    void acceptsGenuineToken() {
        String token = provider.generateAccessToken("user-123", "user@example.org", "ADMIN");
        assertThat(provider.validateToken(token)).isTrue();
    }
}
