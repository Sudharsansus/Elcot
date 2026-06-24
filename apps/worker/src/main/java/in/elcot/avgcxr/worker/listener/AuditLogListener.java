package in.elcot.avgcxr.worker.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Consumes {@code avgc.audit.created} events. Calls the API to persist
 * the audit record in the partitioned {@code audit_log} table.
 *
 * <p>Replaces the previous log-only stub. The audit data is parsed from
 * JSON and forwarded to {@code POST /api/v1/audit-logs} which the
 * AuditLogController handles with a real JPA INSERT.</p>
 */
@Component
public class AuditLogListener {
    private static final Logger log = LoggerFactory.getLogger(AuditLogListener.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiBaseUrl;
    private final String apiInternalToken;

    public AuditLogListener(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${avgcxr.api.base-url:http://localhost:8080}") String apiBaseUrl,
            @Value("${avgcxr.api.internal-token:}") String apiInternalToken) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiBaseUrl = apiBaseUrl;
        this.apiInternalToken = apiInternalToken;
    }

    @RabbitListener(queues = "avgc.audit.created", concurrency = "5-10")
    public void handleAuditLog(Map<String, Object> payload) {
        try {
            // Forward to API which persists via JPA
            HttpHeaders h = new HttpHeaders();
            h.set("X-Internal-Token", apiInternalToken);
            h.set("Content-Type", "application/json");

            ResponseEntity<Map> resp = restTemplate.exchange(
                    apiBaseUrl + "/api/v1/audit-logs",
                    HttpMethod.POST,
                    new HttpEntity<>(payload, h),
                    Map.class
            );
            if (resp.getStatusCode().is2xxSuccessful()) {
                log.debug("Audit log persisted via API: {}", payload.get("eventId"));
            } else {
                log.warn("Audit log persistence returned {}", resp.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Audit log persistence failed for payload: {}",
                    payload != null ? payload.get("eventId") : "null", e);
            throw new RuntimeException(e);
        }
    }
}
