package in.elcot.avgcxr.chat.application.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Tamil language detection and transliteration helpers.
 *
 * <p>The chat agent responds in Tamil when the user's input contains
 * Tamil script (Unicode range U+0B80-U+0BFF) OR when the session
 * language is set to 'ta'.</p>
 */
@Service
public class TamilSupport {

    private static final Pattern TAMIL_SCRIPT = Pattern.compile("[\\u0B80-\\u0BFF]");

    public boolean containsTamilScript(String text) {
        if (text == null) return false;
        return TAMIL_SCRIPT.matcher(text).find();
    }

    public String detectLanguage(String userInput, String sessionLanguage) {
        if (containsTamilScript(userInput)) return "ta";
        if (sessionLanguage != null && (sessionLanguage.equalsIgnoreCase("ta")
                || sessionLanguage.equalsIgnoreCase("tamil"))) {
            return "ta";
        }
        return "en";
    }

    /** Quick translation of common portal terms (en → ta). For full
     *  translation, the LLM does it; this is a fallback for the rule-based
     *  provider and for snippets in the UI. */
    public String translateTerm(String english) {
        if (english == null) return null;
        return switch (english.toLowerCase()) {
            case "scheme" -> "திட்டம்";
            case "application" -> "விண்ணப்பம்";
            case "subsidy" -> "மானியம்";
            case "eligibility" -> "தகுதி";
            case "documents" -> "ஆவணங்கள்";
            case "status" -> "நிலை";
            case "register" -> "பதிவு செய்க";
            case "login" -> "உள்நுழைய";
            case "password" -> "கடவுச்சொல்";
            case "help" -> "உதவி";
            case "grievance" -> "புகார்";
            case "home" -> "முகப்பு";
            case "apply" -> "விண்ணப்பிக்க";
            case "district" -> "மாவட்டம்";
            case "tamil nadu" -> "தமிழ்நாடு";
            default -> english;
        };
    }
}
