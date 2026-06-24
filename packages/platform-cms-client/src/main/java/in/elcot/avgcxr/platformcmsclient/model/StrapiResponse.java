package in.elcot.avgcxr.platformcmsclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Generic Strapi REST API response wrapper.
 * Maps the standard Strapi v4 response format with meta and data fields.
 *
 * @param <T> The type of the data attribute items
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StrapiResponse<T> {

    private Meta meta;
    private List<StrapiDataItem<T>> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<StrapiDataItem<T>> getData() {
        return data;
    }

    public void setData(List<StrapiDataItem<T>> data) {
        this.data = data;
    }

    /**
     * Strapi pagination and other metadata.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private Pagination pagination;

        @JsonProperty("pagination")
        public Pagination getPagination() {
            return pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }
    }

    /**
     * Pagination information from Strapi response.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pagination {
        private int page;
        private int pageSize;
        private int pageCount;
        private long total;

        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        public int getPageCount() { return pageCount; }
        public void setPageCount(int pageCount) { this.pageCount = pageCount; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
    }

    /**
     * Wrapper for individual Strapi data items with id and attributes.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StrapiDataItem<T> {
        private int id;
        private Map<String, Object> attributes;
        private T data;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        @JsonProperty("attributes")
        public Map<String, Object> getAttributes() { return attributes; }
        public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

        @JsonProperty("data")
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }
}
