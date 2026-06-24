package in.elcot.avgcxr.platform.file.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class FileMetadataAlreadyExistsException extends ConflictException {
    public FileMetadataAlreadyExistsException(String field, String value) {
        super("FILEMETADATA_DUPLICATE", "FileMetadata already exists with " + field + ": " + value);
    }
}
