package in.elcot.avgcxr.platform.file.application.port.input;

import in.elcot.avgcxr.platform.file.api.rest.dto.response.FileMetadataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetFileMetadataUseCase {
    FileMetadataResponse getById(UUID id);
    Page<FileMetadataResponse> findAll(Pageable pageable);
}
