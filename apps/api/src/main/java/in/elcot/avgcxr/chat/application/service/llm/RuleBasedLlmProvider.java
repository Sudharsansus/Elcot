package in.elcot.avgcxr.chat.application.service.llm;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * Rule-based LLM provider -- fallback when no external LLM is configured.
 *
 * <p>Matches the user's message against a set of regex rules (English + Tamil) and returns a canned
 * response. Always available, zero cost.
 *
 * <p>Activated when no other LlmProvider bean is available (e.g. {@code
 * avgcxr.llm.provider=rule-based} or unset).
 */
@Service
@ConditionalOnMissingBean(value = LlmProvider.class, ignored = RuleBasedLlmProvider.class)
public class RuleBasedLlmProvider implements LlmProvider {

  private final List<Rule> rules =
      List.of(
          new Rule(
              Pattern.compile("(?i)\\b(scheme|subsid|grant|fund|avgc|tamil nadu|tn)\\b"),
              "The Tamil Nadu AVGC-XR Portal offers several schemes: "
                  + "1) Animation/VFX Subsidy (up to ₹50L), 2) Gaming Studio Grant, "
                  + "3) Comics Publisher Support, 4) XR Innovation Fund, "
                  + "5) Skill Development Initiative. Browse all at /schemes."),
          new Rule(
              Pattern.compile("(?i)\\b(apply|application|how to|submit)\\b"),
              "To apply: 1) Register at /register, 2) Browse /schemes, "
                  + "3) Click Apply on a scheme, 4) Fill the form, "
                  + "5) Upload required documents (Aadhaar, PAN, GST, project proposal), "
                  + "6) Submit. Track status in My Applications."),
          new Rule(
              Pattern.compile("(?i)\\b(status|track|progress|where)\\b"),
              "Track your application at /applications/mine. "
                  + "You'll get email + SMS updates on every state change."),
          new Rule(
              Pattern.compile("(?i)\\b(eligibility|eligible|criteria|qualify)\\b"),
              "Common eligibility: Tamil Nadu resident, registered business "
                  + "(GST/Shop Act), project plan, budget breakdown. "
                  + "Each scheme has its own criteria -- see the scheme detail page."),
          new Rule(
              Pattern.compile("(?i)\\b(document|upload|paper|file)\\b"),
              "Required documents: Aadhaar, PAN, GST certificate, "
                  + "company registration, project proposal, budget breakdown, "
                  + "ID proof. Each scheme has a specific list -- see the form."),
          new Rule(
              Pattern.compile("(?i)\\b(password|forgot|reset|login|sign in)\\b"),
              "Reset password at /forgot-password. We email a reset link "
                  + "(1-hour expiry). For account lockout (5 failed attempts), "
                  + "wait 15 minutes or contact support."),
          new Rule(
              Pattern.compile("(?i)\\b(contact|help|support|grievance|complaint)\\b"),
              "Help: Helpdesk at /helpdesk. Grievance at /grievance. "
                  + "DPDP Act: we acknowledge within 7 days, resolve within 30."),
          new Rule(
              Pattern.compile("(?i)\\b(dpdp|privacy|data|consent)\\b"),
              "We comply with the Digital Personal Data Protection Act 2023. "
                  + "Your consent is recorded per DPDP_DATA_PROCESSING. "
                  + "You can withdraw consent any time. See /privacy."),
          new Rule(
              Pattern.compile("(?i)\\b(tamil|தமிழ்|language|translate)\\b"),
              "Yes, full Tamil (தமிழ்) support. Use the language toggle "
                  + "in the header. The AI chat agent also responds in Tamil."),
          new Rule(
              Pattern.compile("(?i)\\b(scheme|திட்டம்|நிதி|மானியம்)\\b"),
              "தமிழ்நாடு AVGC-XR Portal-ல் பல திட்டங்கள் உள்ளன: "
                  + "அனிமேஷன்/VFX மானியம், கேமிங் ஸ்டுடியோ, XR நிதி, "
                  + "காமிக்ஸ் வெளியீட்டாளர் ஆதரவு. அனைத்தையும் /schemes-ல் பார்க்கவும்."));

  private final Rule fallback =
      new Rule(
          Pattern.compile(".*"),
          "I'm Mira, the AVGC-XR Portal assistant. I can help with: "
              + "schemes, applications, status, eligibility, documents, password, "
              + "grievance, Tamil language. Try: \"What schemes are available?\" "
              + "or \"How do I apply?\" -- or write in Tamil!");

  @Override
  public String name() {
    return "rule-based";
  }

  @Override
  public boolean isAvailable() {
    return true;
  }

  @Override
  public LlmResponse complete(
      String systemPrompt, List<Map<String, String>> messages, LlmOptions options) {
    long start = System.currentTimeMillis();
    // Get the last user message
    String lastUser = "";
    for (int i = messages.size() - 1; i >= 0; i--) {
      Map<String, String> m = messages.get(i);
      if ("user".equalsIgnoreCase(m.get("role"))) {
        lastUser = m.get("content") == null ? "" : m.get("content");
        break;
      }
    }
    String content = match(lastUser);
    return LlmResponse.of(content, "rule-based-v1", 0, 0, System.currentTimeMillis() - start);
  }

  private String match(String input) {
    if (input == null || input.isBlank()) return fallback.response;
    for (Rule r : rules) {
      if (r.pattern.matcher(input).find()) {
        return r.response;
      }
    }
    return fallback.response;
  }

  private record Rule(Pattern pattern, String response) {}
}
