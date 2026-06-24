package in.elcot.avgcxr.worker.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Consumes {@code avgc.notification.dispatch} events and routes to
 * email/SMS/push channels based on the {@code channel} field.
 *
 * <p>Replaces the previous log-only stub that sent a hardcoded
 * {@code recipient@example.com} email. Now the actual recipient and
 * channel from the event payload are used.</p>
 */
@Component
public class NotificationListener {
    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    private final RestTemplate restTemplate;
    private final String apiBaseUrl;
    private final String apiInternalToken;

    public NotificationListener(
            RestTemplate restTemplate,
            @Value("${avgcxr.api.base-url:http://localhost:8080}") String apiBaseUrl,
            @Value("${avgcxr.api.internal-token:}") String apiInternalToken) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
        this.apiInternalToken = apiInternalToken;
    }

    @RabbitListener(queues = "avgc.notification.dispatch", concurrency = "5-10")
    public void handleNotification(Map<String, Object> payload) {
        String channel = payload != null && payload.get("channel") != null
                ? payload.get("channel").toString().toUpperCase() : "EMAIL";
        String eventType = payload != null && payload.get("eventType") != null
                ? payload.get("eventType").toString() : "GENERIC";
        log.info("Dispatching {} via {}", eventType, channel);

        try {
            HttpHeaders h = new HttpHeaders();
            h.set("X-Internal-Token", apiInternalToken);
            h.set("Content-Type", "application/json");

            String endpoint = switch (channel) {
                case "SMS" -> "/api/v1/notifications/sms";
                case "PUSH" -> "/api/v1/notifications/push";
                default -> "/api/v1/notifications/email";
            };
            ResponseEntity<Map> resp = restTemplate.exchange(
                    apiBaseUrl + endpoint,
                    HttpMethod.POST,
                    new HttpEntity<>(payload, h),
                    Map.class
            );
            if (resp.getStatusCode().is2xxSuccessful()) {
                log.info("Notification {} delivered via {}", eventType, channel);
            } else {
                log.warn("Notification delivery returned {}", resp.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Notification delivery failed for {} via {}", eventType, channel, e);
            throw new RuntimeException(e);
        }
    }
}
