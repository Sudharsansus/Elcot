package in.elcot.avgcxr.policy.workflow.delegate;

import in.elcot.avgcxr.common.infrastructure.config.RabbitMQConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

/**
 * Flowable service-task delegate that publishes a rejection notification
 * to RabbitMQ ({@code avgc.notification.dispatch}). Downstream consumers
 * (email/SMS listeners) will pick it up and contact the applicant.
 *
 * <p>Uses {@link ObjectProvider} so the bean can be constructed even when
 * RabbitAutoConfiguration is excluded (e.g. in tests).</p>
 */
@Component("rejectionNotificationDelegate")
public class RejectionNotificationDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(RejectionNotificationDelegate.class);

    private final ObjectProvider<RabbitTemplate> rabbitTemplateProvider;

    public RejectionNotificationDelegate(ObjectProvider<RabbitTemplate> rabbitTemplateProvider) {
        this.rabbitTemplateProvider = rabbitTemplateProvider;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String applicationId = execution.getVariable("applicationId") != null
                ? execution.getVariable("applicationId").toString() : null;
        String applicantId = execution.getVariable("applicantId") != null
                ? execution.getVariable("applicantId").toString() : null;
        String reason = execution.getVariable("rejectionReason") != null
                ? execution.getVariable("rejectionReason").toString() : "Not specified";
        String applicantEmail = execution.getVariable("applicantEmail") != null
                ? execution.getVariable("applicantEmail").toString() : null;
        String applicantPhone = execution.getVariable("applicantPhone") != null
                ? execution.getVariable("applicantPhone").toString() : null;

        log.info("Publishing rejection notification for application={} applicant={}",
                applicationId, applicantId);

        Map<String, Object> payload = Map.of(
                "eventType", "APPLICATION_REJECTED",
                "applicationId", applicationId == null ? "" : applicationId,
                "applicantId", applicantId == null ? "" : applicantId,
                "applicantEmail", applicantEmail == null ? "" : applicantEmail,
                "applicantPhone", applicantPhone == null ? "" : applicantPhone,
                "reason", reason,
                "channel", "BOTH",
                "subject", "Application Rejected — " + applicationId,
                "body", "Your application " + applicationId + " has been rejected. Reason: " + reason,
                "occurredAt", Instant.now().toString()
        );

        RabbitTemplate rt = rabbitTemplateProvider.getIfAvailable();
        if (rt != null) {
            rt.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.RK_NOTIFICATION_DISPATCH,
                    payload
            );
        } else {
            log.info("[DEV ONLY — no RabbitMQ] Would publish rejection notification: {}", payload);
        }
    }
}
