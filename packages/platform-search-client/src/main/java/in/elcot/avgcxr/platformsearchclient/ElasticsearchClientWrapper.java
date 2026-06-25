package in.elcot.avgcxr.platformsearchclient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import in.elcot.avgcxr.platformsearchclient.model.SearchResult;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Type-safe wrapper around the Elasticsearch Java client. Provides search, index, and delete
 * operations for AVGC-XR Portal documents.
 *
 * <p>Supports bilingual search with Tamil and English analyzers, fuzzy matching, and faceted search
 * via aggregations.
 */
@Component
public class ElasticsearchClientWrapper {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchClientWrapper.class);

  private final ElasticsearchClient client;

  public ElasticsearchClientWrapper(ElasticsearchClient client) {
    this.client = client;
  }

  /**
   * Perform a full-text search across an index.
   *
   * @param indexName the Elasticsearch index name
   * @param documentType class type for deserialization
   * @param queryString the search query text
   * @param from pagination offset (0-based)
   * @param size page size
   * @param <T> document type
   * @return search result with hits and pagination metadata
   */
  public <T> SearchResult<T> search(
      String indexName, Class<T> documentType, String queryString, int from, int size) {
    try {
      Query query =
          Query.of(
              q ->
                  q.multiMatch(
                      m ->
                          m.query(queryString)
                              .fields("name", "name.ta", "description", "description.ta")
                              .fuzziness("AUTO")));

      SearchRequest request =
          SearchRequest.of(
              s ->
                  s.index(indexName)
                      .query(query)
                      .from(from)
                      .size(size)
                      .highlight(
                          h ->
                              h.fields("name", f -> f.preTags("<em>").postTags("</em>"))
                                  .fields(
                                      "description", f -> f.preTags("<em>").postTags("</em>"))));

      SearchResponse<T> response = client.search(request, documentType);
      return mapToSearchResult(response, from, size);
    } catch (IOException e) {
      log.error(
          "Elasticsearch search failed for index: {}, query: {}, error: {}",
          indexName,
          queryString,
          e.getMessage());
      throw new SearchClientException("Search failed: " + e.getMessage(), e);
    }
  }

  /**
   * Index a document.
   *
   * @param indexName index name
   * @param documentId document ID
   * @param document the document to index
   * @param <T> document type
   */
  public <T> void index(String indexName, String documentId, T document) {
    try {
      client.index(i -> i.index(indexName).id(documentId).document(document));
      log.debug("Document indexed: {}/{}", indexName, documentId);
    } catch (IOException e) {
      log.error(
          "Failed to index document: {}/{}, error: {}", indexName, documentId, e.getMessage());
      throw new SearchClientException("Index failed: " + e.getMessage(), e);
    }
  }

  /**
   * Delete a document from an index.
   *
   * @param indexName index name
   * @param documentId document ID
   */
  public void delete(String indexName, String documentId) {
    try {
      client.delete(d -> d.index(indexName).id(documentId));
      log.debug("Document deleted: {}/{}", indexName, documentId);
    } catch (IOException e) {
      log.error(
          "Failed to delete document: {}/{}, error: {}", indexName, documentId, e.getMessage());
      throw new SearchClientException("Delete failed: " + e.getMessage(), e);
    }
  }

  private <T> SearchResult<T> mapToSearchResult(SearchResponse<T> response, int from, int size) {
    SearchResult<T> result = new SearchResult<>();
    result.setTotalHits(response.hits().total().value());
    result.setFrom(from);
    result.setSize(size);
    result.setTookMillis(response.took());

    List<SearchResult.SearchHit<T>> hits =
        response.hits().hits().stream().map(this::mapHit).collect(Collectors.toList());
    result.setHits(hits);

    return result;
  }

  private <T> SearchResult.SearchHit<T> mapHit(Hit<T> hit) {
    SearchResult.SearchHit<T> searchHit = new SearchResult.SearchHit<>();
    searchHit.setId(hit.id());
    searchHit.setScore(hit.score());
    searchHit.setSource(hit.source());

    if (hit.highlight() != null) {
      Map<String, List<String>> highlightMap =
          hit.highlight().entrySet().stream()
              .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()));
      searchHit.setHighlight(highlightMap);
    }

    return searchHit;
  }

  /** Exception thrown when Elasticsearch operations fail. */
  public static class SearchClientException extends RuntimeException {
    public SearchClientException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
