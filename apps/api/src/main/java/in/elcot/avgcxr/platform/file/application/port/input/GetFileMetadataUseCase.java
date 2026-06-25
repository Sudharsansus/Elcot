package in.elcot.avgcxr.platform.file.application.port.input;

import in.elcot.avgcxr.platform.file.api.rest.dto.response.FileMetadataResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetFileMetadataUseCase {
  FileMetadataResponse getById(UUID id);

  Page<FileMetadataResponse> findAll(Pageable pageable);
}
