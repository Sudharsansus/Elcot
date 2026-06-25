package in.elcot.avgcxr.platform.user.application.port.input;

import in.elcot.avgcxr.platform.user.api.rest.dto.response.UserProfileResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetUserProfileUseCase {
  UserProfileResponse getById(UUID id);

  Page<UserProfileResponse> findAll(Pageable pageable);
}
