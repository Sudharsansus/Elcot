package in.elcot.avgcxr.worker.listener;

import java.util.Map;
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

/**
 * Consumes {@code avgc.application.submitted} events. Calls the API to: 1. Validate the application
 * (404 if missing) 2. Start a Flowable workflow instance 3. Send a confirmation email via the
 * notification queue
 *
 * <p>This is the real implementation that replaces the previous log-only stub. It calls the live
 * API service via HTTP so the worker stays stateless and lightweight.
 */
@Component
public class ApplicationSubmissionListener {
  private static final Logger log = LoggerFactory.getLogger(ApplicationSubmissionListener.class);

  private final RestTemplate restTemplate;
  private final String apiBaseUrl;
  private final String apiInternalToken;

  public ApplicationSubmissionListener(
      RestTemplate restTemplate,
      @Value("${avgcxr.api.base-url:http://localhost:8080}") String apiBaseUrl,
      @Value("${avgcxr.api.internal-token:}") String apiInternalToken) {
    this.restTemplate = restTemplate;
    this.apiBaseUrl = apiBaseUrl;
    this.apiInternalToken = apiInternalToken;
  }

  @RabbitListener(queues = "avgc.application.submitted", concurrency = "3-5")
  public void handleApplicationSubmission(Map<String, Object> payload) {
    long start = System.currentTimeMillis();
    String applicationId =
        payload != null && payload.get("applicationId") != null
            ? payload.get("applicationId").toString()
            : "unknown";
    log.info("Processing application submission: {}", applicationId);
    try {
      HttpHeaders h = new HttpHeaders();
      h.set("X-Internal-Token", apiInternalToken);
      h.set("Content-Type", "application/json");

      // 1. Validate application exists
      ResponseEntity<Map> validateResp =
          restTemplate.exchange(
              apiBaseUrl + "/api/v1/applications/" + applicationId,
              HttpMethod.GET,
              new HttpEntity<>(h),
              Map.class);
      if (!validateResp.getStatusCode().is2xxSuccessful()) {
        log.warn("Application {} not found in API, skipping", applicationId);
        return;
      }

      // 2. Start the application-review Flowable process
      ResponseEntity<Map> startResp =
          restTemplate.exchange(
              apiBaseUrl + "/api/v1/workflows/start",
              HttpMethod.POST,
              new HttpEntity<>(
                  Map.of(
                      "processKey",
                      "applicationReview",
                      "variables",
                      Map.of("applicationId", applicationId)),
                  h),
              Map.class);
      if (startResp.getStatusCode().is2xxSuccessful()) {
        log.info("Workflow started for application {}", applicationId);
      } else {
        log.warn(
            "Workflow start returned {} for application {}",
            startResp.getStatusCode(),
            applicationId);
      }

      // 3. Send confirmation via notification queue
      restTemplate.exchange(
          apiBaseUrl + "/api/v1/notifications/email",
          HttpMethod.POST,
          new HttpEntity<>(
              Map.of(
                  "applicationId",
                  applicationId,
                  "subject",
                  "Application submitted successfully",
                  "body",
                  "Your application " + applicationId + " is under review."),
              h),
          Map.class);
      log.info(
          "Application {} processed in {}ms", applicationId, System.currentTimeMillis() - start);
    } catch (Exception e) {
      log.error("Application submission processing failed for {}", applicationId, e);
      throw new RuntimeException(e); // triggers DLQ
    }
  }
}
