package in.elcot.avgcxr.common.infrastructure.security;

import java.util.regex.Pattern;

/**
 * Safety guard for the PUBLIC, unauthenticated chat assistants (Mira + the chat agent). Because
 * both are reachable without signing in, neither may surface anything beyond public scheme /
 * process information. This guard provides two independent, provider-agnostic controls:
 *
 * <ul>
 *   <li>{@link #looksLikeDataExfil(String)} — blocks messages that try to extract other people's,
 *       bulk, or administrative data, before any retrieval or model call.
 *   <li>{@link #redact(String)} — strips PII patterns from retrieved RAG context so that even if a
 *       personal record ever lands in the search index, it cannot reach the model or the user.
 * </ul>
 */
public final class ChatSafetyGuard {

  private ChatSafetyGuard() {}

  private static final Pattern EMAIL =
      Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
  private static final Pattern PHONE =
      Pattern.compile("(?<!\\d)(?:\\+?91[\\s-]?)?[6-9]\\d{9}(?!\\d)");
  private static final Pattern AADHAAR =
      Pattern.compile("(?<!\\d)\\d{4}\\s?\\d{4}\\s?\\d{4}(?!\\d)");
  private static final Pattern PAN = Pattern.compile("\\b[A-Z]{5}\\d{4}[A-Z]\\b");
  private static final Pattern LONG_NUMERIC = Pattern.compile("(?<!\\d)\\d{12,}(?!\\d)");

  /** Phrases that signal an attempt to pull other people's / bulk / admin data. */
  private static final Pattern EXFIL =
      Pattern.compile(
          "(?i)("
              + "all\\s+(the\\s+)?(users|applicants|accounts|applications|people|members|emails?|"
              + "phone|numbers)"
              + "|list\\s+(of\\s+)?(users|applicants|accounts|emails?|phones?|numbers|people)"
              + "|every(one|\\s+user|\\s+applicant|\\s+account)"
              + "|other\\s+(users|applicants|people|persons?)|someone\\s+else'?s"
              + "|their\\s+(password|email|phone|aadhaar|pan|data|details|address)"
              + "|user\\s+(database|list|table|records|data)|data\\s*base\\s+(dump|of)|export\\s+all"
              + "|(phone|email|contact|mobile)\\s+(numbers?|addresses?|details?|ids?)\\s+(of|for)"
              + "|aadhaar|pan\\s+(number|card)|password\\s+of|credentials?\\s+of"
              + "|personal\\s+(data|details|information)\\s+of|show\\s+me\\s+(all\\s+)?"
              + "(users|applicants|accounts|applications)"
              + ")");

  /** True if the message is trying to extract personal, bulk, or administrative data. */
  public static boolean looksLikeDataExfil(String message) {
    return message != null && EXFIL.matcher(message).find();
  }

  /** Redact common PII (email, phone, Aadhaar, PAN, long account/ID numbers) from free text. */
  public static String redact(String text) {
    if (text == null || text.isBlank()) {
      return text;
    }
    String t = text;
    t = AADHAAR.matcher(t).replaceAll("[redacted]");
    t = PAN.matcher(t).replaceAll("[redacted]");
    t = EMAIL.matcher(t).replaceAll("[redacted]");
    t = PHONE.matcher(t).replaceAll("[redacted]");
    t = LONG_NUMERIC.matcher(t).replaceAll("[redacted]");
    return t;
  }

  /** Bilingual refusal returned to a blocked request. */
  public static String refusalMessage(String lang) {
    if (lang != null && lang.startsWith("ta")) {
      return "மன்னிக்கவும், பிற பயனர்களின் தனிப்பட்ட தகவல்களையோ, கணக்கு அல்லது விண்ணப்ப "
          + "விவரங்களையோ என்னால் வழங்க முடியாது. உங்கள் சொந்த விவரங்களைப் பார்க்க உள்நுழையவும், "
          + "அல்லது /helpdesk-ஐ தொடர்பு கொள்ளவும். பொதுத் திட்டங்கள் குறித்து நான் உதவ முடியும்.";
    }
    return "Sorry, I can't share personal information, or anyone's account or application details. "
        + "To see your own details please sign in, or contact /helpdesk. I can help with public "
        + "information about schemes and the application process.";
  }
}
