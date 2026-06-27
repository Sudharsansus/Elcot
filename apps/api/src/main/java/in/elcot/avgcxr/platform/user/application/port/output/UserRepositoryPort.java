package in.elcot.avgcxr.platform.user.application.port.output;

import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryPort {
  User save(User user);

  /** Paginated list of all users (admin user management). */
  Page<User> findAll(Pageable pageable);

  /**
   * Save the user including their bcrypt password hash in a single INSERT. Required because
   * users.password_hash is NOT NULL but not exposed on the domain User aggregate.
   */
  User saveWithCredentials(User user, String passwordHash);

  Optional<User> findById(UserId id);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByMobileNumber(String mobile);

  boolean existsByEmail(String email);

  boolean existsByMobileNumber(String mobile);
}
