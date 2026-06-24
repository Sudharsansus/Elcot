output "documents_bucket" {
  description = "Documents S3 bucket name"
  value       = aws_s3_bucket.documents.id
}

output "media_bucket" {
  description = "Media S3 bucket name"
  value       = aws_s3_bucket.media.id
}

output "exports_bucket" {
  description = "Exports S3 bucket name"
  value       = aws_s3_bucket.exports.id
}

output "backups_bucket" {
  description = "Backups S3 bucket name"
  value       = aws_s3_bucket.backups.id
}
