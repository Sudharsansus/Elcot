variable "region" {
  description = "AWS region"
  type        = string
  default     = "ap-south-1"
}

variable "availability_zones" {
  description = "List of availability zones"
  type        = list(string)
}

variable "vpc_cidr" {
  description = "VPC CIDR block"
  type        = string
}

variable "db_instance_class" {
  description = "RDS instance class"
  type        = string
}

variable "db_allocated_storage" {
  description = "RDS allocated storage in GB"
  type        = number
}

variable "db_username" {
  description = "RDS master username"
  type        = string
  default     = "avgcxr_app"
}

variable "db_password" {
  description = "RDS master password"
  type        = string
  sensitive   = true
}

variable "redis_node_type" {
  description = "ElastiCache Redis node type"
  type        = string
}

variable "es_instance_type" {
  description = "Elasticsearch instance type"
  type        = string
}

variable "es_volume_size" {
  description = "Elasticsearch volume size in GB"
  type        = number
}

variable "rmq_instance_type" {
  description = "RabbitMQ broker instance type"
  type        = string
}

variable "rmq_admin_password" {
  description = "RabbitMQ admin password"
  type        = string
  sensitive   = true
}

variable "redis_password" {
  description = "ElastiCache Redis auth token / password"
  type        = string
  sensitive   = true
}
