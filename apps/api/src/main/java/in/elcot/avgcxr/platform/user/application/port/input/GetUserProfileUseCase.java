package in.elcot.avgcxr.platform.user.application.port.input;

import in.elcot.avgcxr.platform.user.api.rest.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetUserProfileUseCase {
    UserProfileResponse getById(UUID id);
    Page<UserProfileResponse> findAll(Pageable pageable);
}
