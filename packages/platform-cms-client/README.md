# Platform CMS Client

Strapi headless CMS integration for AVGC-XR Portal.

## Features

- Type-safe Strapi REST API client
- Bilingual content retrieval (EN/TA)
- Response model mapping
- Media URL resolution via MinIO
- Caching with configurable TTL

## Usage

```java
@Autowired
private StrapiClient strapiClient;

public List<SchemeContent> getSchemes(String locale) {
    StrapiResponse<SchemeContent> response = strapiClient
        .get("/api/schemes", SchemeContent.class, Map.of("locale", locale));
    return response.getData();
}
```
