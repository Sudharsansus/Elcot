package in.elcot.avgcxr.platform.user.api.rest;

import in.elcot.avgcxr.platform.user.api.rest.dto.request.CreateUserRequest;
import in.elcot.avgcxr.platform.user.api.rest.dto.request.UpdateUserRequest;
import in.elcot.avgcxr.platform.user.api.rest.dto.response.UserResponse;
import in.elcot.avgcxr.platform.user.application.command.CreateUserCommand;
import in.elcot.avgcxr.platform.user.application.command.UpdateUserCommand;
import in.elcot.avgcxr.platform.user.application.service.UserService;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
    var cmd =
        new CreateUserCommand(
            req.username(),
            req.email(),
            req.mobileNumber(),
            req.fullName(),
            req.fullNameTamil(),
            req.password(),
            req.district(),
            req.roles());
    var user = userService.create(cmd);
    return ResponseEntity.created(URI.create("/api/v1/users/" + user.getId()))
        .body(UserResponse.from(user));
  }

  @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER','DISTRICT_OFFICER')")
  @GetMapping
  public ResponseEntity<Page<UserResponse>> list(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(userService.list(pageable).map(UserResponse::from));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(UserResponse.from(userService.getById(UserId.of(id))));
  }

  @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> update(
      @PathVariable String id, @Valid @RequestBody UpdateUserRequest req) {
    var cmd =
        new UpdateUserCommand(
            req.fullName(),
            req.fullNameTamil(),
            req.district(),
            req.department(),
            req.designation());
    return ResponseEntity.ok(UserResponse.from(userService.update(UserId.of(id), cmd)));
  }
}
