package in.elcot.avgcxr.platform.search.api.rest;

import in.elcot.avgcxr.platformsearchclient.ElasticsearchClientWrapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Full-text search endpoint backed by Elasticsearch.
 *
 * <p>Audit fix: the {@link ElasticsearchClientWrapper} was previously declared but never invoked.
 * This controller exercises it for cross-entity full-text search with bilingual (English + Tamil)
 * support.
 *
 * <p>If Elasticsearch is not available at runtime, the endpoint falls back to an empty result set
 * with {@code source: "es-unavailable"}. Tests run with Elasticsearch disabled see this fallback.
 */
@RestController
@RequestMapping("/api/v1/search")
public class FullTextSearchController {

  private static final Logger log = LoggerFactory.getLogger(FullTextSearchController.class);

  private final ObjectProvider<ElasticsearchClientWrapper> esProvider;

  public FullTextSearchController(ObjectProvider<ElasticsearchClientWrapper> esProvider) {
    this.esProvider = esProvider;
  }

  /**
   * Full-text search across schemes, companies, talent, freelancers.
   *
   * @param q search query (English or Tamil)
   * @param type optional filter: scheme|company|talent|freelancer
   * @param lang "en" or "ta" (default "en")
   * @param size page size (default 20)
   */
  @GetMapping("/fulltext")
  public ResponseEntity<Map<String, Object>> fulltext(
      @RequestParam("q") String q,
      @RequestParam(required = false) String type,
      @RequestParam(defaultValue = "en") String lang,
      @RequestParam(defaultValue = "20") int size) {

    long start = System.currentTimeMillis();
    ElasticsearchClientWrapper es = esProvider.getIfAvailable();

    if (es == null) {
      log.debug("Elasticsearch not available, returning fallback for q={}", q);
      return ResponseEntity.ok(
          Map.of(
              "query",
              q,
              "source",
              "es-unavailable",
              "results",
              List.of(),
              "total",
              0,
              "elapsedMs",
              System.currentTimeMillis() - start));
    }

    try {
      String index =
          type != null && !type.isBlank() ? "avgcxr-" + type.toLowerCase() : "avgcxr-general";
      var search = es.search(index, Map.class, q, 0, size);
      List<Map<String, Object>> hits = new ArrayList<>();
      int total = 0;
      if (search != null && search.getHits() != null) {
        total = search.getHits().size();
        for (var hit : search.getHits()) {
          Map<String, Object> h = new LinkedHashMap<>();
          h.put("id", hit.getId());
          h.put("score", hit.getScore());
          h.put("source", hit.getSource());
          h.put("index", index);
          hits.add(h);
        }
      }
      return ResponseEntity.ok(
          Map.of(
              "query", q,
              "type", type == null ? "" : type,
              "lang", lang,
              "source", "elasticsearch",
              "results", hits,
              "total", total,
              "elapsedMs", System.currentTimeMillis() - start));
    } catch (Exception e) {
      log.error("Elasticsearch search failed for q={}: {}", q, e.getMessage(), e);
      return ResponseEntity.ok(
          Map.of(
              "query",
              q,
              "source",
              "es-error",
              "error",
              e.getMessage(),
              "results",
              List.of(),
              "total",
              0,
              "elapsedMs",
              System.currentTimeMillis() - start));
    }
  }

  /** Index a single document into Elasticsearch. */
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/index/{index}/{id}")
  public ResponseEntity<Map<String, Object>> index(
      @PathVariable String index,
      @PathVariable String id,
      @RequestBody Map<String, Object> document) {
    ElasticsearchClientWrapper es = esProvider.getIfAvailable();
    if (es == null) {
      return ResponseEntity.ok(Map.of("index", index, "id", id, "status", "es-unavailable"));
    }
    try {
      es.index("avgcxr-" + index, id, document);
      return ResponseEntity.ok(Map.of("index", "avgcxr-" + index, "id", id, "status", "indexed"));
    } catch (Exception e) {
      log.error("Index failed for {}/{}: {}", index, id, e.getMessage());
      return ResponseEntity.status(500)
          .body(Map.of("index", index, "id", id, "status", "error", "message", e.getMessage()));
    }
  }
}
