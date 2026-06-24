package in.elcot.avgcxr.platform.user.application.port.input;

import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import java.util.Optional;

/**
 * Domain-specific user lookup port. The auth-layer use case
 * {@code in.elcot.avgcxr.platform.auth.application.port.input.GetUserUseCase}
 * is a different, larger interface that this implementation also fulfils.
 */
public interface GetUserUseCase {
    Optional<User> findById(UserId id);
    User getById(UserId id);
    User getByUsername(String username);
}
