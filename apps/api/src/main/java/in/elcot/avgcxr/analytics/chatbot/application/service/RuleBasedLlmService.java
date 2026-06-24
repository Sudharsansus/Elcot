package in.elcot.avgcxr.analytics.chatbot.application.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Default rule-based LLM implementation.
 *
 * <p>Audit fix: extracted the regex matcher out of MiraChatbotService into
 * a proper {@link LlmService} implementation. The service can later be
 * swapped for a real LLM by removing this bean and adding an OpenAI /
 * Anthropic / local-model implementation of {@link LlmService}.</p>
 *
 * <p>Activated only when no other LLMService bean is present, so a real
 * LLM implementation will override it without code changes to Mira.</p>
 */
@Service
@ConditionalOnMissingBean(value = LlmService.class, ignored = RuleBasedLlmService.class)
public class RuleBasedLlmService implements LlmService {

    private final List<Rule> rules = List.of(
            new Rule(Pattern.compile("(?i)scheme|subsid|tamil nadu|avgc|tnd"),
                    "You can browse all schemes at /schemes. " +
                    "Subsidies range from ₹2L to ₹50L depending on the scheme and project size. " +
                    "Eligibility typically requires being a Tamil Nadu resident with a registered business."),
            new Rule(Pattern.compile("(?i)apply|application|how to|submit"),
                    "To apply: 1) Register at /register, 2) Browse schemes, 3) Click Apply, " +
                    "4) Fill the form, 5) Upload required documents, 6) Submit. " +
                    "Track status in 'My Applications'."),
            new Rule(Pattern.compile("(?i)status|track|progress"),
                    "You can track your application status at /applications/mine. " +
                    "Updates are sent via email and SMS."),
            new Rule(Pattern.compile("(?i)eligibility|eligible|criteria"),
                    "Eligibility varies by scheme. Common criteria: Tamil Nadu resident, " +
                    "registered business, GST (if applicable), clearances, prior project experience. " +
                    "See /schemes for detailed criteria per scheme."),
            new Rule(Pattern.compile("(?i)document|upload|paper"),
                    "Common required documents: GST certificate, PAN, company registration, " +
                    "project proposal, budget breakdown, ID proof. " +
                    "Check the specific scheme's document checklist."),
            new Rule(Pattern.compile("(?i)contact|help|support|grievance"),
                    "For help: 1) Helpdesk at /helpdesk, 2) Call 044-XXXXXXX, " +
                    "3) File a grievance at /grievance. " +
                    "We aim to acknowledge within 7 days and resolve within 30 days (DPDP Act)."),
            new Rule(Pattern.compile("(?i)password|forgot|reset|login"),
                    "To reset your password: 1) Go to /forgot-password, 2) Enter your email, " +
                    "3) Check email for reset link, 4) Link expires in 1 hour."),
            new Rule(Pattern.compile("(?i)dpdp|privacy|data"),
                    "We comply with the Digital Personal Data Protection Act 2023. " +
                    "Your consent is recorded for each data processing activity. " +
                    "See /privacy for details."),
            new Rule(Pattern.compile("(?i)language|tamil|தமிழ்|translate"),
                    "The portal supports English and Tamil. Use the language toggle in the header.")
    );

    @Override
    public Map<String, Object> generate(String userMessage, List<Map<String, Object>> context, String lang) {
        // RAG-aware: prepend retrieved context to the response when available
        StringBuilder reply = new StringBuilder();
        if (context != null && !context.isEmpty()) {
            reply.append("Based on the relevant schemes: ");
            for (int i = 0; i < Math.min(3, context.size()); i++) {
                Map<String, Object> doc = context.get(i);
                Object name = doc.get("name");
                Object desc = doc.get("description");
                if (name != null) {
                    reply.append("\n• ").append(name);
                    if (desc != null) reply.append(" — ").append(desc);
                }
            }
            reply.append("\n\n");
        }

        for (Rule r : rules) {
            if (r.pattern.matcher(userMessage).find()) {
                reply.append(r.reply);
                Map<String, Object> out = new LinkedHashMap<>();
                out.put("reply", reply.toString());
                out.put("model", "rule-based-v1");
                out.put("matchedRule", r.pattern.pattern());
                out.put("lang", lang);
                if (context != null && !context.isEmpty()) {
                    out.put("sources", context.stream().map(d -> String.valueOf(d.get("id"))).toList());
                }
                return out;
            }
        }

        // Default
        String defaultReply = "I'm Mira, your AVGC-XR Portal assistant. I can help with schemes, applications, eligibility, and account issues. Please rephrase your question or contact support at /helpdesk.";
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("reply", reply.toString() + defaultReply);
        out.put("model", "rule-based-v1");
        out.put("lang", lang);
        return out;
    }

    private record Rule(Pattern pattern, String reply) {}
}
