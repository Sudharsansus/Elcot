# ADR-0011: Elasticsearch for Search

## Status

Accepted

## Date

2026-02-01

## Context

The portal needs full-text search across schemes, applications, and CMS content. Search must support bilingual queries (English + Tamil), fuzzy matching, faceted filtering (by category, district, status), and relevance ranking. PostgreSQL's native full-text search is insufficient for the required search quality, especially for Tamil language support and real-time indexing.

## Decision

Use Elasticsearch 8 as the search engine. Documents are indexed asynchronously via RabbitMQ events. The `platform-search-client` package provides a type-safe Java wrapper. A custom Tamil analyzer with stemming support is configured at index creation. Search results are served through the search bounded context API.

## Consequences

### Positive

- Near-real-time search (documents searchable within 1 second of creation).
- Tamil language support via custom analyzer with Tamil stemmer.
- Fuzzy matching handles spelling variations common in transliterated Tamil.
- Faceted search enables filter-driven navigation (category, district, status).
- Highlighting shows matched text snippets in search results.

### Negative

- Elasticsearch is memory-intensive (recommended 50% of available RAM for JVM heap).
- Data consistency is eventually consistent (not transactional with PostgreSQL).
- Additional infrastructure component to deploy, monitor, and back up.

### Risks

- Risk: Elasticsearch cluster becomes unavailable, disabling search. Mitigation: Search is a secondary feature; the portal remains functional without it. Return empty results with a degradation notice.