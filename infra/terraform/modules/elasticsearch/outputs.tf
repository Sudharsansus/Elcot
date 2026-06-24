output "es_endpoint" {
  description = "Elasticsearch domain endpoint"
  value       = aws_opensearch_domain.main.endpoint
}

output "es_domain_arn" {
  description = "Elasticsearch domain ARN"
  value       = aws_opensearch_domain.main.arn
}

output "es_domain_name" {
  description = "Elasticsearch domain name"
  value       = aws_opensearch_domain.main.domain_name
}
