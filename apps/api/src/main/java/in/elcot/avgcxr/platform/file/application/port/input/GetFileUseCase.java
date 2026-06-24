package in.elcot.avgcxr.platform.file.application.port.input;

import in.elcot.avgcxr.platform.file.domain.model.File;
import in.elcot.avgcxr.platform.file.domain.model.FileId;
import java.util.Optional;

public interface GetFileUseCase {
    Optional<File> findById(FileId id);
    File getById(FileId id);
}
