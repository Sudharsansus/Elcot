output "rmq_endpoint" {
  description = "RabbitMQ AMQP endpoint"
  value       = "amqps://${aws_mq_broker.main.instances[0].endpoints[0]}"
}

output "rmq_management_url" {
  description = "RabbitMQ management console URL"
  value       = "https://${aws_mq_broker.main.instances[0].endpoints[1]}"
}

output "rmq_broker_id" {
  description = "RabbitMQ broker ID"
  value       = aws_mq_broker.main.id
}
