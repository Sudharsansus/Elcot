package in.elcot.avgcxr.platform.cms.api.rest;

import in.elcot.avgcxr.platformcmsclient.StrapiClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CMS proxy endpoints that front the Strapi instance.
 *
 * <p>Audit fix: the {@link StrapiClient} was previously declared but never invoked. These endpoints
 * exercise it for the public portal: scheme descriptions, FAQs, news, and announcements.
 */
@RestController
@RequestMapping("/api/v1/cms")
public class CmsController {

  private static final Logger log = LoggerFactory.getLogger(CmsController.class);

  private final StrapiClient strapiClient;

  public CmsController(StrapiClient strapiClient) {
    this.strapiClient = strapiClient;
  }

  @GetMapping("/schemes/{id}/description")
  public ResponseEntity<Map<String, Object>> getSchemeDescription(@PathVariable String id) {
    try {
      var resp = strapiClient.get("/api/scheme-descriptions/" + id, Map.class, null);
      return ResponseEntity.ok(
          Map.of(
              "id", id,
              "title", readAttr(resp, "title"),
              "titleTamil", readAttr(resp, "titleTamil"),
              "body", readAttr(resp, "body"),
              "bodyTamil", readAttr(resp, "bodyTamil")));
    } catch (Exception e) {
      log.warn("Strapi scheme description fetch failed for id={}: {}", id, e.getMessage());
      return ResponseEntity.ok(
          Map.of(
              "id", id,
              "source", "fallback",
              "message", "CMS content not available"));
    }
  }

  @GetMapping("/faqs")
  public ResponseEntity<List<Object>> getFaqs(@RequestParam(defaultValue = "en") String lang) {
    try {
      Map<String, String> params = new HashMap<>();
      params.put("locale", lang);
      params.put("populate", "translations");
      var resp = strapiClient.get("/api/faqs", Map.class, params);
      Object data = resp != null ? resp.getData() : null;
      if (data instanceof List<?> list) {
        return ResponseEntity.ok((List<Object>) list);
      }
      return ResponseEntity.ok(List.of());
    } catch (Exception e) {
      log.warn("Strapi FAQs fetch failed: {}", e.getMessage());
      return ResponseEntity.ok(List.of());
    }
  }

  @GetMapping("/news")
  public ResponseEntity<List<Object>> getNews(
      @RequestParam(defaultValue = "en") String lang,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    try {
      Map<String, String> params = new HashMap<>();
      params.put("locale", lang);
      params.put("populate", "translations,image");
      params.put("pagination[page]", String.valueOf(page));
      params.put("pagination[pageSize]", String.valueOf(size));
      var resp = strapiClient.get("/api/news", Map.class, params);
      Object data = resp != null ? resp.getData() : null;
      if (data instanceof List<?> list) {
        return ResponseEntity.ok((List<Object>) list);
      }
      return ResponseEntity.ok(List.of());
    } catch (Exception e) {
      log.warn("Strapi news fetch failed: {}", e.getMessage());
      return ResponseEntity.ok(List.of());
    }
  }

  @GetMapping("/pages/{slug}")
  public ResponseEntity<Map<String, Object>> getPage(@PathVariable String slug) {
    try {
      Map<String, String> params = new HashMap<>();
      params.put("filters[slug][$eq]", slug);
      var resp = strapiClient.get("/api/pages", Map.class, params);
      return ResponseEntity.ok(
          Map.of(
              "slug", slug,
              "raw", resp));
    } catch (Exception e) {
      log.warn("Strapi page fetch failed for slug={}: {}", slug, e.getMessage());
      return ResponseEntity.ok(
          Map.of(
              "slug", slug,
              "source", "fallback",
              "message", "CMS content not available"));
    }
  }

  private static String readAttr(Object resp, String key) {
    if (resp == null) return "";
    try {
      var data = resp.getClass().getMethod("getData").invoke(resp);
      if (data instanceof Map<?, ?> m) {
        Object attrs = m.get("attributes");
        if (attrs instanceof Map<?, ?> am) {
          Object v = am.get(key);
          return v == null ? "" : v.toString();
        }
      }
    } catch (Exception e) {
      log.debug("readAttr failed for key={}: {}", key, e.getMessage());
    }
    return "";
  }
}
