output "redis_endpoint" {
  description = "Redis primary endpoint"
  value       = aws_elasticache_replication_group.main.primary_endpoint_address
}

output "redis_port" {
  description = "Redis port"
  value       = 6379
}

output "redis_connection_url" {
  description = "Redis connection URL"
  value       = "rediss://${aws_elasticache_replication_group.main.primary_endpoint_address}:6379"
}
