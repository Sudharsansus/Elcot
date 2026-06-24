variable "region" {
  type = string
}
variable "environment" {
  type = string
}
variable "project_name" {
  type    = string
  default = "avgcxr-portal"
}
variable "vpc_id" {
  type = string
}
variable "subnet_ids" {
  type = list(string)
}
variable "db_instance_class" {
  type    = string
  default = "db.r6g.large"
}
variable "db_instance_count" {
  type    = number
  default = 2
}
variable "db_allocated_storage" {
  type    = number
  default = 100
}
variable "db_username" {
  type    = string
  default = "avgcxr_app"
}
variable "db_password" {
  type      = string
  sensitive = true
}
variable "backup_retention_days" {
  type    = number
  default = 7
}
variable "allowed_cidr_blocks" {
  type    = list(string)
  default = ["10.0.0.0/16"]
}
