resource "aws_db_subnet_group" "main" {
  name       = "${var.project_name}-${var.environment}-db-subnet-group"
  subnet_ids = var.subnet_ids

  tags = {
    Name = "${var.project_name}-${var.environment}-db-subnet-group"
  }
}

resource "aws_security_group" "db" {
  name        = "${var.project_name}-${var.environment}-db-sg"
  vpc_id      = var.vpc_id
  description = "Security group for PostgreSQL RDS"

  ingress {
    from_port   = 5432
    to_port     = 5432
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

resource "aws_rds_cluster" "main" {
  cluster_identifier           = "${var.project_name}-${var.environment}"
  engine                       = "aurora-postgresql"
  engine_version               = "16.1"
  database_name                = "avgcxr_portal"
  master_username              = var.db_username
  master_password              = var.db_password
  db_subnet_group_name         = aws_db_subnet_group.main.name
  vpc_security_group_ids       = [aws_security_group.db.id]
  backup_retention_period      = var.backup_retention_days
  preferred_backup_window      = "03:00-04:00"
  preferred_maintenance_window = "sun:04:00-sun:05:00"
  skip_final_snapshot          = var.environment == "dev" ? true : false
  final_snapshot_identifier    = var.environment != "dev" ? "${var.project_name}-${var.environment}-final-snapshot" : null
  storage_encrypted            = true
  deletion_protection          = var.environment == "prod" ? true : false

  enabled_cloudwatch_logs_exports = ["postgresql"]

  tags = {
    Name = "${var.project_name}-${var.environment}-rds"
  }
}

resource "aws_rds_cluster_instance" "instances" {
  count                        = var.db_instance_count
  cluster_identifier           = aws_rds_cluster.main.id
  instance_class               = var.db_instance_class
  engine                       = aws_rds_cluster.main.engine
  engine_version               = aws_rds_cluster.main.engine_version
  publicly_accessible          = false
  monitoring_interval          = 60
  performance_insights_enabled = true
}
