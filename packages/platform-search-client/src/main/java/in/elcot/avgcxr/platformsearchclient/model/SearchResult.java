package in.elcot.avgcxr.platformsearchclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Generic search result wrapper for Elasticsearch queries.
 * Contains pagination metadata, result items, and aggregation data.
 *
 * @param <T> the type of search result items
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult<T> {

    private long totalHits;
    private long from;
    private int size;
    private List<SearchHit<T>> hits;
    private Map<String, AggregationResult> aggregations;
    private long tookMillis;

    public long getTotalHits() { return totalHits; }
    public void setTotalHits(long totalHits) { this.totalHits = totalHits; }

    public long getFrom() { return from; }
    public void setFrom(long from) { this.from = from; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public List<SearchHit<T>> getHits() { return hits; }
    public void setHits(List<SearchHit<T>> hits) { this.hits = hits; }

    public Map<String, AggregationResult> getAggregations() { return aggregations; }
    public void setAggregations(Map<String, AggregationResult> aggregations) { this.aggregations = aggregations; }

    public long getTookMillis() { return tookMillis; }
    public void setTookMillis(long tookMillis) { this.tookMillis = tookMillis; }

    public long getTotalPages() {
        return size > 0 ? (totalHits + size - 1) / size : 0;
    }

    /**
     * Represents a single search hit with score and source document.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchHit<T> {
        private String id;
        private double score;
        private T source;
        private Map<String, List<String>> highlight;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
        public T getSource() { return source; }
        public void setSource(T source) { this.source = source; }
        public Map<String, List<String>> getHighlight() { return highlight; }
        public void setHighlight(Map<String, List<String>> highlight) { this.highlight = highlight; }
    }

    /**
     * Aggregation bucket result for faceted search.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AggregationResult {
        private List<AggregationBucket> buckets;

        public List<AggregationBucket> getBuckets() { return buckets; }
        public void setBuckets(List<AggregationBucket> buckets) { this.buckets = buckets; }
    }

    /**
     * Single aggregation bucket with key and document count.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AggregationBucket {
        private String key;
        private long docCount;

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public long getDocCount() { return docCount; }
        public void setDocCount(long docCount) { this.docCount = docCount; }
    }
}
