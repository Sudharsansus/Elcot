package in.elcot.avgcxr.platform.notification.application.service;

import in.elcot.avgcxr.common.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final RabbitTemplate rabbitTemplate;

    public NotificationService(@org.springframework.context.annotation.Lazy RabbitTemplate rabbitTemplate) { this.rabbitTemplate = rabbitTemplate; }

    public void sendNotification(String userId, String subject, String body) {
        log.info("Sending notification to user {}: {}", userId, subject);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.RK_NOTIFICATION_DISPATCH,
                new NotificationPayload(userId, subject, body));
    }

    public void sendSms(String mobileNumber, String message) {
        log.info("Sending SMS to {}: {}", mobileNumber, message.length() > 50 ? message.substring(0, 50) + "..." : message);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.RK_NOTIFICATION_SMS,
                java.util.Map.of("mobileNumber", mobileNumber, "message", message));
    }

    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to {}: {}", to, subject);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.RK_NOTIFICATION_EMAIL,
                new EmailPayload(to, subject, body));
    }

    public void sendOtp(String mobileNumber, String otp) {
        log.info("Sending OTP to {}", mobileNumber);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.RK_NOTIFICATION_SMS,
                new OtpPayload(mobileNumber, otp));
    }

    private record NotificationPayload(String userId, String subject, String body) {}
    private record EmailPayload(String to, String subject, String body) {}
    private record OtpPayload(String mobileNumber, String otp) {}
}
