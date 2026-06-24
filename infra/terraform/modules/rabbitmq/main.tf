resource "aws_mq_broker" "main" {
  broker_name         = "${var.project_name}-${var.environment}-rabbitmq"
  engine_type         = "RabbitMQ"
  engine_version      = "3.13"
  host_instance_type  = var.rmq_instance_type
  deployment_mode     = var.environment == "prod" ? "CLUSTER_MULTI_AZ" : "SINGLE_INSTANCE"
  publicly_accessible = false

  user {
    username = "avgcxr_rmq_admin"
    password = var.rmq_admin_password
  }

  configuration {
    id       = aws_mq_configuration.main.id
    revision = aws_mq_configuration.main.latest_revision
  }

  subnet_ids      = var.subnet_ids
  security_groups = [aws_security_group.rmq.id]

  maintenance_window_start_time {
    day_of_week = "SUNDAY"
    time_of_day = "04:00"
    time_zone   = "Asia/Kolkata"
  }

  encryption_options {
    use_aws_owned_key = true
  }

  logs {
    general = true
    audit   = true
  }

  tags = {
    Name = "${var.project_name}-${var.environment}-rabbitmq"
  }
}

resource "aws_mq_configuration" "main" {
  description    = "AVGC-XR RabbitMQ configuration"
  name           = "${var.project_name}-${var.environment}-rmq-config"
  engine_type    = "RabbitMQ"
  engine_version = "3.13"

  data = jsonencode({
    defaultUser = "avgcxr_rmq_admin"
    rabbitmq = {
      auth_mechanisms          = ["PLAIN", "AMQPLAIN"]
      vm_memory_high_watermark = { relative = 0.6 }
      disk_free_limit          = { absolute = 2147483648 }
    }
  })
}

resource "aws_security_group" "rmq" {
  name        = "${var.project_name}-${var.environment}-rmq-sg"
  vpc_id      = var.vpc_id
  description = "Security group for RabbitMQ"

  ingress {
    from_port   = 5671
    to_port     = 5672
    protocol    = "tcp"
    cidr_blocks = var.allowed_cidr_blocks
  }

  ingress {
    from_port   = 15671
    to_port     = 15672
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
