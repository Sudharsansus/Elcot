resource "aws_opensearch_domain" "main" {
  domain_name    = "${var.project_name}-${var.environment}-es"
  engine_version = "Elasticsearch_8.11"

  cluster_config {
    instance_type          = var.es_instance_type
    instance_count         = var.es_instance_count
    zone_awareness_enabled = var.es_instance_count > 1
  }

  vpc_options {
    subnet_ids         = var.subnet_ids
    security_group_ids = [aws_security_group.es.id]
  }

  ebs_options {
    ebs_enabled = true
    volume_type = "gp3"
    volume_size = var.es_volume_size
  }

  encrypt_at_rest {
    enabled = true
  }

  domain_endpoint_options {
    enforce_https       = true
    tls_security_policy = "Policy-Min-TLS-1-2-2019-07"
  }

  advanced_security_options {
    enabled                        = false
    anonymous_auth_enabled         = false
    internal_user_database_enabled = false
  }

  tags = {
    Name = "${var.project_name}-${var.environment}-elasticsearch"
  }
}

resource "aws_security_group" "es" {
  name        = "${var.project_name}-${var.environment}-es-sg"
  vpc_id      = var.vpc_id
  description = "Security group for Elasticsearch"

  ingress {
    from_port   = 9200
    to_port     = 9200
    protocol    = "tcp"
    cidr_blocks = var.allowed_cidr_blocks
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
