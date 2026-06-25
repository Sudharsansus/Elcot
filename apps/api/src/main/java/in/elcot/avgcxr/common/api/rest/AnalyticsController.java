package in.elcot.avgcxr.common.api.rest;

import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Frontend analytics event ingestion endpoint. The admin portal batches UI events (clicks, page
 * views) and sends them here.
 */
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

  @PostMapping("/events")
  @PreAuthorize("isAuthenticated()")
  public Map<String, Object> ingest(@RequestBody Map<String, Object> body) {
    // Real impl: push to Elasticsearch or a Kafka topic
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> events =
        (List<Map<String, Object>>) body.getOrDefault("events", List.of());
    return Map.of("accepted", events.size());
  }
}
