package in.elcot.avgcxr.platform.file.application.port.output;

import in.elcot.avgcxr.platform.file.domain.model.File;
import in.elcot.avgcxr.platform.file.domain.model.FileId;
import java.util.Optional;

public interface FileRepositoryPort {
  File save(File file);

  Optional<File> findById(FileId id);

  void deleteById(FileId id);
}
