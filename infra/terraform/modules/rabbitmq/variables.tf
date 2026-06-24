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
variable "rmq_instance_type" {
  type    = string
  default = "mq.r6g.large"
}
variable "rmq_admin_password" {
  type      = string
  sensitive = true
}
variable "allowed_cidr_blocks" {
  type    = list(string)
  default = ["10.0.0.0/16"]
}
