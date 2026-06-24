package in.elcot.avgcxr.platformcmsclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.elcot.avgcxr.platformcmsclient.model.StrapiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Map;

/**
 * Type-safe client for Strapi v4 REST API.
 * Handles bilingual content retrieval, media URL resolution, and error mapping.
 *
 * <p>Used by bounded contexts to fetch CMS-managed content such as scheme
 * descriptions, FAQs, news articles, and banner images.</p>
 */
@Component
public class StrapiClient {

    private static final Logger log = LoggerFactory.getLogger(StrapiClient.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String apiToken;

    public StrapiClient(
            @Value("${avgcxr.cms.base-url:http://localhost:1337}") String baseUrl,
            @Value("${avgcxr.cms.api-token:}") String apiToken) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.apiToken = apiToken;
    }

    /**
     * Fetch content from Strapi API.
     *
     * @param path      API path (e.g., "/api/schemes")
     * @param responseType the class to deserialize the data into
     * @param params    query parameters (locale, populate, filters, etc.)
     * @param <T>       response data type
     * @return StrapiResponse containing the requested data
     */
    public <T> StrapiResponse<T> get(String path, Class<T> responseType, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + path);

        if (params != null) {
            params.forEach(builder::queryParam);
        }

        String url = builder.toUriString();
        log.debug("Strapi request: GET {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiToken != null && !apiToken.isBlank()) {
            headers.setBearerAuth(apiToken);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new StrapiClientException(
                    "Strapi API error: " + response.getStatusCode(),
                    response.getStatusCode().value());
        }

        try {
            return objectMapper.readValue(response.getBody(),
                    objectMapper.getTypeFactory().constructParametricType(
                            StrapiResponse.class, responseType));
        } catch (Exception e) {
            throw new StrapiClientException("Failed to parse Strapi response: " + e.getMessage(), 500);
        }
    }

    /**
     * Fetch a single entry from Strapi API by ID.
     *
     * @param path      API path (e.g., "/api/schemes")
     * @param id        entry ID
     * @param responseType the class to deserialize into
     * @param <T>       response data type
     * @return StrapiResponse with single item
     */
    public <T> StrapiResponse<T> getById(String path, int id, Class<T> responseType,
                                          Map<String, String> params) {
        return get(path + "/" + id, responseType, params);
    }

    /**
     * Resolve media URL to absolute URL.
     * Converts relative Strapi media paths to full URLs.
     *
     * @param relativePath relative path from Strapi media
     * @return absolute URL for the media asset
     */
    public String resolveMediaUrl(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return null;
        }
        if (relativePath.startsWith("http")) {
            return relativePath;
        }
        return baseUrl + relativePath;
    }

    /**
     * Exception thrown when Strapi API calls fail.
     */
    public static class StrapiClientException extends RuntimeException {
        private final int statusCode;

        public StrapiClientException(String message, int statusCode) {
            super(message);
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}
