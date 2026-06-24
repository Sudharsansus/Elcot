package in.elcot.avgcxr.policy.scheme.application.port.input;

import in.elcot.avgcxr.policy.scheme.application.command.ProcessSchemeCommand;
import in.elcot.avgcxr.policy.scheme.domain.model.SchemeId;

public interface ProcessSchemeUseCase {
    void process(SchemeId id, ProcessSchemeCommand command);
}
