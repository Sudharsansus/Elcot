package in.elcot.avgcxr.platformnotificationclient;

import in.elcot.avgcxr.platformnotificationclient.email.EmailRequest;
import in.elcot.avgcxr.platformnotificationclient.sms.SmsRequest;

/**
 * Gateway interface for multi-channel notification delivery. Provides a unified API for sending
 * notifications across email and SMS channels.
 *
 * <p>Implementations route notifications to appropriate channel handlers and support both
 * synchronous and asynchronous delivery modes.
 */
public interface NotificationGateway {

  /**
   * Send an email notification.
   *
   * @param request the email request containing recipient, template, and parameters
   */
  void sendEmail(EmailRequest request);

  /**
   * Send an SMS notification.
   *
   * @param request the SMS request containing recipient, template, and parameters
   */
  void sendSms(SmsRequest request);

  /**
   * Send an email notification asynchronously. The notification is placed on a RabbitMQ queue for
   * background processing.
   *
   * @param request the email request
   */
  void sendEmailAsync(EmailRequest request);

  /**
   * Send an SMS notification asynchronously. The notification is placed on a RabbitMQ queue for
   * background processing.
   *
   * @param request the SMS request
   */
  void sendSmsAsync(SmsRequest request);
}
