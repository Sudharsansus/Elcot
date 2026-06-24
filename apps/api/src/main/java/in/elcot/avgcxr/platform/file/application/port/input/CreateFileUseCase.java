package in.elcot.avgcxr.platform.file.application.port.input;

import in.elcot.avgcxr.platform.file.application.command.CreateFileCommand;
import in.elcot.avgcxr.platform.file.domain.model.File;

public interface CreateFileUseCase {
    File create(CreateFileCommand command);
}
