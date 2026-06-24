terraform {
  required_version = ">= 1.5.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {
    bucket  = "avgcxr-portal-tfstate-staging"
    key     = "infra/staging/terraform.tfstate"
    region  = "ap-south-1"
    encrypt = true
  }
}

provider "aws" {
  region = var.region
  default_tags {
    tags = {
      Project     = "avgcxr-portal"
      Environment = "staging"
      ManagedBy   = "terraform"
      Owner       = "ELCOT"
      CostCenter  = "AVGC-XR-STAGING"
    }
  }
}

module "network" {
  source      = "../../modules/network"
  region      = var.region
  environment = "staging"
  vpc_cidr    = var.vpc_cidr
  azs         = var.availability_zones
}

module "database" {
  source               = "../../modules/database"
  region               = var.region
  environment          = "staging"
  vpc_id               = module.network.vpc_id
  subnet_ids           = module.network.private_subnet_ids
  db_instance_class    = var.db_instance_class
  db_allocated_storage = var.db_allocated_storage
  db_username          = var.db_username
  db_password          = var.db_password
}

module "redis" {
  source          = "../../modules/redis"
  region          = var.region
  environment     = "staging"
  vpc_id          = module.network.vpc_id
  subnet_ids      = module.network.private_subnet_ids
  redis_node_type = var.redis_node_type
}

module "elasticsearch" {
  source           = "../../modules/elasticsearch"
  region           = var.region
  environment      = "staging"
  vpc_id           = module.network.vpc_id
  subnet_ids       = module.network.private_subnet_ids
  es_instance_type = var.es_instance_type
  es_volume_size   = var.es_volume_size
}

module "rabbitmq" {
  source             = "../../modules/rabbitmq"
  region             = var.region
  environment        = "staging"
  vpc_id             = module.network.vpc_id
  subnet_ids         = module.network.private_subnet_ids
  rmq_instance_type  = var.rmq_instance_type
  rmq_admin_password = var.rmq_admin_password
}

module "object_storage" {
  source      = "../../modules/object-storage"
  region      = var.region
  environment = "staging"
}

module "secrets" {
  source         = "../../modules/secrets"
  region         = var.region
  environment    = "staging"
  db_password    = var.db_password
  redis_password = var.redis_password
  rmq_password   = var.rmq_admin_password
}
