package in.elcot.avgcxr.platform.user.application.port.output;

import in.elcot.avgcxr.platform.user.domain.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepositoryPort {
    UserProfile save(UserProfile entity);
    Optional<UserProfile> findById(UUID id);
    Page<UserProfile> findAll(Pageable pageable);
    void deleteById(UUID id);
}
