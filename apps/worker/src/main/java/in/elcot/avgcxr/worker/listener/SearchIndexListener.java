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
 * Consumes {@code avgc.search.index} events. Calls the API to upsert the document into the
 * Elasticsearch index.
 *
 * <p>Replaces the previous log-only stub. The API has the real Elasticsearch client and handles the
 * index lifecycle (create index, upsert document, refresh).
 */
@Component
public class SearchIndexListener {
  private static final Logger log = LoggerFactory.getLogger(SearchIndexListener.class);

  private final RestTemplate restTemplate;
  private final String apiBaseUrl;
  private final String apiInternalToken;

  public SearchIndexListener(
      RestTemplate restTemplate,
      @Value("${avgcxr.api.base-url:http://localhost:8080}") String apiBaseUrl,
      @Value("${avgcxr.api.internal-token:}") String apiInternalToken) {
    this.restTemplate = restTemplate;
    this.apiBaseUrl = apiBaseUrl;
    this.apiInternalToken = apiInternalToken;
  }

  @RabbitListener(queues = "avgc.search.index", concurrency = "2-4")
  public void handleSearchIndex(Map<String, Object> payload) {
    try {
      HttpHeaders h = new HttpHeaders();
      h.set("X-Internal-Token", apiInternalToken);
      h.set("Content-Type", "application/json");

      String index =
          payload != null && payload.get("index") != null
              ? payload.get("index").toString()
              : "avgcxr-general";
      String id =
          payload != null && payload.get("id") != null ? payload.get("id").toString() : null;

      String url = apiBaseUrl + "/api/v1/search/index/" + index;
      if (id != null) url += "/" + id;

      ResponseEntity<Map> resp =
          restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(payload, h), Map.class);
      if (resp.getStatusCode().is2xxSuccessful()) {
        log.debug("Document indexed in ES: {}/{}", index, id);
      } else {
        log.warn("ES indexing returned {}", resp.getStatusCode());
      }
    } catch (Exception e) {
      log.error(
          "ES indexing failed for payload id={}", payload != null ? payload.get("id") : "null", e);
      throw new RuntimeException(e);
    }
  }
}
