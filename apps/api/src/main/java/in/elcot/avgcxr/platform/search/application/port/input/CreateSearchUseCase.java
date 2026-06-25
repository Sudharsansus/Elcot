package in.elcot.avgcxr.platform.search.application.port.input;

import in.elcot.avgcxr.platform.search.application.command.CreateSearchCommand;
import in.elcot.avgcxr.platform.search.domain.model.Search;

public interface CreateSearchUseCase {
  Search create(CreateSearchCommand command);
}
