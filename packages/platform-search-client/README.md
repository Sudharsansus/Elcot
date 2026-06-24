# Platform Search Client

Elasticsearch client wrapper for AVGC-XR Portal.

## Features

- Type-safe Elasticsearch operations
- Bilingual indexing (English + Tamil text analysis)
- Fuzzy search support
- Faceted search and aggregations
- Search result highlighting

## Usage

```java
@Autowired
private ElasticsearchClientWrapper searchClient;

SearchResult<SchemeDocument> result = searchClient.search(
    "avgcxr-schemes",
    SchemeDocument.class,
    "animation startup fund",
    0, 20
);
```
