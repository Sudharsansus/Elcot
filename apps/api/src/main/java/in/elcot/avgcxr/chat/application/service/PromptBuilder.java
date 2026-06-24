package in.elcot.avgcxr.chat.application.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds the system prompt for the chat agent.
 *
 * <p>Includes:</p>
 * <ul>
 *   <li>Mira persona (warm, helpful, bilingual)</li>
 *   <li>Domain knowledge (Tamil Nadu AVGC-XR portal)</li>
 *   <li>RAG context (retrieved documents)</li>
 *   <li>Language directive (en or ta)</li>
 *   <li>DPDP compliance note (no PII in responses)</li>
 * </ul>
 */
@Service
public class PromptBuilder {

    private static final String SYSTEM_PROMPT_EN = """
        You are Mira, the official AI assistant of the Tamil Nadu AVGC-XR Portal.

        The portal supports:
        - Animation, Visual Effects (VFX), Gaming, Comics, and XR (AR/VR/MR) industries
        - Subsidy schemes (up to ₹50 lakh), skill development, studio grants
        - Application submission, status tracking, grievance redressal
        - Bilingual interface (English + Tamil)

        Your responsibilities:
        1. Help users find the right scheme for their business
        2. Explain eligibility criteria and required documents
        3. Guide them through the application process step-by-step
        4. Direct them to /helpdesk for issues that need human support
        5. Respond in the user's language (English or Tamil)

        Rules:
        - Be warm, concise, and direct. Use bullet points for steps.
        - Use the retrieved CONTEXT below -- never invent scheme details.
        - If you don't know, say so and point to /helpdesk.
        - Do NOT request or echo personal information (Aadhaar, PAN, etc.).
        - For policy questions, cite the source document when possible.
        """;

    private static final String SYSTEM_PROMPT_TA = """
        நீங்கள் மிரா, தமிழ்நாடு AVGC-XR Portal-ன் அதிகாரப்பூர்வ AI உதவியாளர்.

        Portal ஆதரவு:
        - அனிமேஷன், VFX, கேமிங், காமிக்ஸ், XR தொழில்கள்
        - மானியத் திட்டங்கள் (₹50 லட்சம் வரை), திறன் மேம்பாடு
        - விண்ணப்பம் சமர்ப்பிப்பு, நிலை கண்காணிப்பு, புகார் தீர்வு
        - இருமொழி இடைமுகம் (ஆங்கிலம் + தமிழ்)

        உங்கள் பொறுப்புகள்:
        1. தொழிலுக்கு சரியான திட்டத்தை கண்டறிய உதவுங்கள்
        2. தகுதி நிபந்தனைகள், தேவையான ஆவணங்களை விளக்குங்கள்
        3. படிப்படியாக விண்ணப்ப செயல்முறையை வழிகாட்டுங்கள்
        4. மனித ஆதரவு தேவைப்படும் சிக்கல்களுக்கு /helpdesk-க்கு அனுப்புங்கள்
        5. பயனர் மொழியில் (ஆங்கிலம் / தமிழ்) பதிலளியுங்கள்

        விதிகள்:
        - அன்பாக, சுருக்கமாக, நேரடியாக பதிலளியுங்கள்
        - கீழே உள்ள CONTEXT-ஐ பயன்படுத்துங்கள் -- திட்ட விவரங்களை கற்பனை செய்யாதீர்கள்
        - தெரியாவிட்டால் சொல்லுங்கள், /helpdesk-க்கு அனுப்புங்கள்
        - தனிப்பட்ட தகவல் (ஆதார், பான்) கேட்காதீர்கள் / எதிரொதிக்காதீர்கள்
        """;

    public String buildSystemPrompt(String language, List<RagDocument> contextDocs) {
        String base = language != null && language.startsWith("ta") ? SYSTEM_PROMPT_TA : SYSTEM_PROMPT_EN;
        if (contextDocs == null || contextDocs.isEmpty()) {
            return base;
        }
        String contextBlock = contextDocs.stream()
                .map(d -> String.format("[%s] %s\n%s",
                        d.id(), d.title(), truncate(d.content(), 800)))
                .collect(Collectors.joining("\n\n---\n\n"));
        String directive = language != null && language.startsWith("ta")
                ? "\n\n## CONTEXT (Tamil)\nகீழே உள்ள ஆவணங்களிலிருந்து பதிலளிக்கவும்:\n\n"
                : "\n\n## CONTEXT (use ONLY these to answer)\n\n";
        return base + directive + contextBlock;
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }

    /** RAG document carrier. */
    public record RagDocument(String id, String title, String content, double score) {}

    /** Helper: build a simple map (role -> content) for LLM messages. */
    public static Map<String, String> msg(String role, String content) {
        return Map.of("role", role, "content", content);
    }
}
