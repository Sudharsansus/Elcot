package in.elcot.avgcxr.analytics.chatbot.api.rest;

import in.elcot.avgcxr.analytics.chatbot.application.service.MiraChatbotService;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Mira chatbot REST endpoints. Bilingual (English + Tamil). */
@RestController
@RequestMapping("/api/v1/mira")
public class MiraController {

  private final MiraChatbotService miraService;

  public MiraController(MiraChatbotService miraService) {
    this.miraService = miraService;
  }

  @PreAuthorize("permitAll()")
  @PostMapping("/chat")
  public Map<String, Object> chat(
      @RequestBody Map<String, String> body,
      @RequestParam(value = "lang", defaultValue = "en") String lang) {
    return miraService.answer(body.get("message"), lang);
  }

  @GetMapping("/suggestions")
  public List<Map<String, String>> suggestions() {
    return miraService.suggestedQuestions();
  }
}
