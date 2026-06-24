package in.elcot.avgcxr.platform.auth.application.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Multi-factor authentication (MFA) using TOTP (RFC 6238).
 *
 * <p>Used for ADMIN, NODAL_OFFICER, and DISTRICT_OFFICER roles per
 * tender requirements.</p>
 */
@Service
public class MfaService {

    private static final int SECRET_BYTES = 20;
    private static final int TIME_STEP_SECONDS = 30;
    private static final int CODE_DIGITS = 6;
    private static final int WINDOW = 1;  // accept ±1 time step

    @Value("${app.name:AVGC-XR Portal}")
    private String appName;

    /**
     * Generate a new TOTP secret + QR code URL for the user.
     * The user scans the QR code with Google Authenticator / Microsoft Authenticator.
     */
    public MfaEnrollment enroll(String userEmail) {
        byte[] secret = new byte[SECRET_BYTES];
        new SecureRandom().nextBytes(secret);
        String secretBase32 = new Base32().encodeAsString(secret).replace("=", "");

        String otpAuthUrl = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s&digits=%d&period=%d",
                URLEncoder.encode(appName, StandardCharsets.UTF_8),
                URLEncoder.encode(userEmail, StandardCharsets.UTF_8),
                secretBase32,
                URLEncoder.encode(appName, StandardCharsets.UTF_8),
                CODE_DIGITS,
                TIME_STEP_SECONDS);

        return new MfaEnrollment(secretBase32, otpAuthUrl);
    }

    /**
     * Generate a QR code PNG as byte[] for embedding in /auth/mfa/setup page.
     */
    public byte[] generateQrCodePng(String otpAuthUrl, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);

            var matrix = new QRCodeWriter().encode(otpAuthUrl, BarcodeFormat.QR_CODE, width, height, hints);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return out.toByteArray();
        } catch (WriterException | IOException ex) {
            throw new IllegalStateException("Failed to generate QR code", ex);
        }
    }

    /**
     * Verify a 6-digit TOTP code against the user's secret.
     * Allows ±1 time step window (so codes work for 90 seconds total).
     */
    public boolean verifyCode(String secretBase32, String code) {
        if (secretBase32 == null || code == null || code.length() != CODE_DIGITS) {
            return false;
        }
        long timeWindow = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;
        for (int i = -WINDOW; i <= WINDOW; i++) {
            String generated = generateCode(secretBase32, timeWindow + i);
            if (constantTimeEquals(generated, code)) {
                return true;
            }
        }
        return false;
    }

    private String generateCode(String secretBase32, long timeWindow) {
        try {
            byte[] key = new Base32().decode(secretBase32);
            byte[] data = ByteBuffer.allocate(8).putLong(timeWindow).array();

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0x0F;
            int binary = ((hash[offset] & 0x7F) << 24)
                       | ((hash[offset + 1] & 0xFF) << 16)
                       | ((hash[offset + 2] & 0xFF) << 8)
                       | (hash[offset + 3] & 0xFF);
            int otp = binary % (int) Math.pow(10, CODE_DIGITS);

            return String.format("%0" + CODE_DIGITS + "d", otp);
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalArgumentException ex) {
            throw new IllegalStateException("TOTP generation failed", ex);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }

    public record MfaEnrollment(String secret, String otpAuthUrl) {}
}
