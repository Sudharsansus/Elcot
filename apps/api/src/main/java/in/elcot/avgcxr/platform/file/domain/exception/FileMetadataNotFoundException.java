package in.elcot.avgcxr.platform.file.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class FileMetadataNotFoundException extends NotFoundException {
    public FileMetadataNotFoundException(UUID id) {
        super("FILEMETADATA_NOT_FOUND", "FileMetadata not found with id: " + id);
    }
}
