package in.elcot.avgcxr.platform.user.application.port.output;

import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    /**
     * Save the user including their bcrypt password hash in a single
     * INSERT. Required because users.password_hash is NOT NULL but
     * not exposed on the domain User aggregate.
     */
    User saveWithCredentials(User user, String passwordHash);

    Optional<User> findById(UserId id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobile);
}
