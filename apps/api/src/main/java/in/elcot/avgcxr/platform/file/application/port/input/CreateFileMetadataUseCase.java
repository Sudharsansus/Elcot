package in.elcot.avgcxr.platform.file.application.port.input;

import in.elcot.avgcxr.platform.file.api.rest.dto.response.FileMetadataResponse;
import in.elcot.avgcxr.platform.file.application.command.CreateFileMetadataCommand;

public interface CreateFileMetadataUseCase {
  FileMetadataResponse create(CreateFileMetadataCommand command);
}
