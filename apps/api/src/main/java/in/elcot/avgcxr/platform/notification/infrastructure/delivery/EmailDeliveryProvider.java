package in.elcot.avgcxr.platform.notification.infrastructure.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Real email delivery via SMTP (tender gateway requirement). Env-gated: a {@link JavaMailSender}
 * bean is only auto-configured when {@code spring.mail.host} is set, and sending also requires
 * {@code avgcxr.mail.enabled=true} and a non-blank {@code avgcxr.mail.from}. When unconfigured the
 * provider logs only (no PII body) and never fails — safe to deploy inert.
 */
@Component
public class EmailDeliveryProvider {

  private static final Logger log = LoggerFactory.getLogger(EmailDeliveryProvider.class);

  private final ObjectProvider<JavaMailSender> mailSenderProvider;
  private final boolean enabled;
  private final String from;

  public EmailDeliveryProvider(
      ObjectProvider<JavaMailSender> mailSenderProvider,
      @Value("${avgcxr.mail.enabled:false}") boolean enabled,
      @Value("${avgcxr.mail.from:}") String from) {
    this.mailSenderProvider = mailSenderProvider;
    this.enabled = enabled;
    this.from = from;
  }

  public boolean send(String to, String subject, String body) {
    JavaMailSender sender = mailSenderProvider.getIfAvailable();
    if (!enabled || sender == null || from == null || from.isBlank()) {
      log.info("[email:log-only] to={} subject='{}' (SMTP not configured)", to, subject);
      return false;
    }
    try {
      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setFrom(from);
      msg.setTo(to);
      msg.setSubject(subject);
      msg.setText(body);
      sender.send(msg);
      log.info("Email delivered to {}", to);
      return true;
    } catch (Exception e) {
      log.error("Email delivery failed to {}: {}", to, e.getMessage());
      return false;
    }
  }
}
