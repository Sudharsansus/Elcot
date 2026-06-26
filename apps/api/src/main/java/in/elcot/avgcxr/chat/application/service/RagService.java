package in.elcot.avgcxr.chat.application.service;

import in.elcot.avgcxr.chat.application.service.PromptBuilder.RagDocument;
import in.elcot.avgcxr.common.infrastructure.security.ChatSafetyGuard;
import in.elcot.avgcxr.platformsearchclient.ElasticsearchClientWrapper;
import in.elcot.avgcxr.platformsearchclient.model.SearchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * RAG (Retrieval-Augmented Generation) service.
 *
 * <p>Pulls relevant documents for the user's query from:
 *
 * <ol>
 *   <li><b>Elasticsearch</b> (when configured) -- full-text, semantic, bilingual
 *   <li><b>PostgreSQL ILIKE fallback</b> -- searches schemes + help content
 * </ol>
 *
 * <p>Returns the top-K documents to be inlined in the LLM system prompt.
 */
@Service
public class RagService {

  private static final Logger log = LoggerFactory.getLogger(RagService.class);
  private static final int DEFAULT_TOP_K = 5;

  private final ObjectProvider<ElasticsearchClientWrapper> esProvider;
  private final ObjectProvider<JdbcTemplate> jdbcProvider;

  public RagService(
      ObjectProvider<ElasticsearchClientWrapper> esProvider,
      ObjectProvider<JdbcTemplate> jdbcProvider) {
    this.esProvider = esProvider;
    this.jdbcProvider = jdbcProvider;
  }

  public List<RagDocument> retrieve(String userQuery, String language) {
    if (userQuery == null || userQuery.isBlank()) return List.of();
    List<RagDocument> docs = new ArrayList<>();
    // 1) Try Elasticsearch first
    ElasticsearchClientWrapper es = esProvider.getIfAvailable();
    if (es != null) {
      try {
        SearchResult<?> r =
            es.search("avgcxr-general", (Class<?>) Map.class, userQuery, 0, DEFAULT_TOP_K);
        if (r != null && r.getHits() != null) {
          for (var hit : r.getHits()) {
            Object src = hit.getSource();
            if (src instanceof Map<?, ?> m) {
              String id = String.valueOf(m.get("id") == null ? hit.getId() : m.get("id"));
              Object titleObj = m.get("title");
              String title = titleObj == null ? "" : titleObj.toString();
              Object bodyObj = m.get("body");
              if (bodyObj == null) bodyObj = m.get("description");
              String body = bodyObj == null ? "" : bodyObj.toString();
              // PII safety net: never let a personal record retrieved from the index reach the
              // model or the user, regardless of what was indexed.
              docs.add(
                  new RagDocument(
                      id,
                      ChatSafetyGuard.redact(title),
                      ChatSafetyGuard.redact(body),
                      hit.getScore()));
            }
          }
        }
      } catch (Exception e) {
        log.warn("ES RAG search failed: {}", e.getMessage());
      }
    }
    // 2) PostgreSQL fallback / supplement
    if (docs.size() < DEFAULT_TOP_K) {
      docs.addAll(searchPostgres(userQuery, DEFAULT_TOP_K - docs.size()));
    }
    return docs;
  }

  @SuppressWarnings("unchecked")
  private List<RagDocument> searchPostgres(String query, int limit) {
    JdbcTemplate jdbc = jdbcProvider.getIfAvailable();
    if (jdbc == null) return List.of();
    try {
      String like = "%" + query.replace("%", "") + "%";
      // Pull from schemes (canonical source of truth) + cms_faq if exists
      return jdbc.query(
          "SELECT id::text, name, COALESCE(description, '') "
              + "FROM schemes WHERE status = 'PUBLISHED' "
              + "  AND (LOWER(name) LIKE LOWER(?) OR LOWER(COALESCE(description, '')) LIKE LOWER(?)) "
              + "ORDER BY updated_at DESC NULLS LAST LIMIT ?",
          (rs, n) ->
              new RagDocument(
                  rs.getString(1),
                  ChatSafetyGuard.redact(rs.getString(2)),
                  ChatSafetyGuard.redact(rs.getString(3)),
                  0.5),
          like,
          like,
          limit);
    } catch (Exception e) {
      log.debug("Postgres RAG fallback failed: {}", e.getMessage());
      return List.of();
    }
  }
}
