package in.elcot.avgcxr.platformnotificationclient.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Email delivery service for AVGC-XR Portal. Supports bilingual HTML email templates
 * (English/Tamil) using Thymeleaf.
 *
 * <p>Email templates are resolved from classpath:/templates/email/ with language-specific suffixes
 * (e.g., scheme-submitted_en.html, scheme-submitted_ta.html).
 */
@Service
public class EmailService {

  private static final Logger log = LoggerFactory.getLogger(EmailService.class);

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;
  private final String fromAddress;
  private final boolean enabled;

  public EmailService(
      JavaMailSender mailSender,
      TemplateEngine templateEngine,
      @Value("${avgcxr.notification.email.from:noreply@elcot.tn.gov.in}") String fromAddress,
      @Value("${avgcxr.notification.email.enabled:true}") boolean enabled) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.fromAddress = fromAddress;
    this.enabled = enabled;
  }

  /**
   * Send an HTML email using a Thymeleaf template.
   *
   * @param to recipient email address
   * @param subject email subject
   * @param templateName template name (without language suffix)
   * @param locale language code ("en" or "ta")
   * @param variables template variables
   */
  public void sendHtmlEmail(
      String to,
      String subject,
      String templateName,
      String locale,
      Map<String, Object> variables) {
    if (!enabled) {
      log.info("Email sending disabled. Would have sent to: {} subject: {}", to, subject);
      return;
    }

    try {
      String templatePath = "email/" + templateName + "_" + locale;
      Context context = new Context();
      context.setVariable("locale", locale);
      if (variables != null) {
        variables.forEach(context::setVariable);
      }

      String htmlContent = templateEngine.process(templatePath, context);

      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper =
          new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
      helper.setFrom(fromAddress);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);

      mailSender.send(message);
      log.info(
          "Email sent successfully to: {}, template: {}, locale: {}", to, templateName, locale);
    } catch (MessagingException e) {
      log.error(
          "Failed to send email to: {}, template: {}, error: {}", to, templateName, e.getMessage());
      throw new EmailDeliveryException("Failed to send email: " + e.getMessage(), e);
    }
  }

  /**
   * Send a simple plain text email.
   *
   * @param to recipient email address
   * @param subject email subject
   * @param body email body text
   */
  public void sendPlainTextEmail(String to, String subject, String body) {
    if (!enabled) {
      log.info("Email sending disabled. Would have sent to: {}", to);
      return;
    }

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
      helper.setFrom(fromAddress);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, false);

      mailSender.send(message);
      log.info("Plain text email sent to: {}", to);
    } catch (MessagingException e) {
      log.error("Failed to send plain text email to: {}, error: {}", to, e.getMessage());
      throw new EmailDeliveryException("Failed to send email: " + e.getMessage(), e);
    }
  }

  /** Exception thrown when email delivery fails. */
  public static class EmailDeliveryException extends RuntimeException {
    public EmailDeliveryException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}

/** Immutable request object for email notifications. (Definition in EmailRequest.java) */
