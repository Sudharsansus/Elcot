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
variable "es_instance_type" {
  type    = string
  default = "r6g.large.search"
}
variable "es_instance_count" {
  type    = number
  default = 1
}
variable "es_volume_size" {
  type    = number
  default = 100
}
variable "allowed_cidr_blocks" {
  type    = list(string)
  default = ["10.0.0.0/16"]
}
