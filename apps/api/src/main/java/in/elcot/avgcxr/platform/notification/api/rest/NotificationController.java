package in.elcot.avgcxr.platform.notification.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.notification.api.rest.dto.response.NotificationResponse;
import in.elcot.avgcxr.platform.notification.application.command.CreateNotificationCommand;
import in.elcot.avgcxr.platform.notification.application.port.input.CreateNotificationUseCase;
import in.elcot.avgcxr.platform.notification.application.port.input.GetNotificationUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Multi-channel notification dispatch (SMS, Email) with template support and
 * delivery tracking.
 *
 * <p>Provides CRUD operations and domain-specific actions following the hexagonal architecture
 * pattern.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
  private final CreateNotificationUseCase createNotificationUseCase;
  private final GetNotificationUseCase getNotificationUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<NotificationResponse>>> list(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<NotificationResponse> results = getNotificationUseCase.findAll(pageable);
    return ResponseEntity.ok(
        ApiResponse.paginated(
            results.getContent(),
            pageable.getPageNumber(),
            pageable.getPageSize(),
            results.getTotalElements()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<NotificationResponse>> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(getNotificationUseCase.getById(id)));
  }

  @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
  @PostMapping
  public ResponseEntity<ApiResponse<NotificationResponse>> create(
      @Valid @RequestBody CreateNotificationCommand command) {
    return ResponseEntity.ok(ApiResponse.success(createNotificationUseCase.create(command)));
  }
}
