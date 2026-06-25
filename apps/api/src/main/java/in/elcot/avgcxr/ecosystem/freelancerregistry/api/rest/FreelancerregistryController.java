package in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest;

import in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.request.CreateFreelancerregistryRequest;
import in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.request.UpdateFreelancerregistryRequest;
import in.elcot.avgcxr.ecosystem.freelancerregistry.api.rest.dto.response.FreelancerregistryResponse;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.command.CreateFreelancerregistryCommand;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.command.UpdateFreelancerregistryCommand;
import in.elcot.avgcxr.ecosystem.freelancerregistry.application.service.FreelancerregistryService;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.Freelancerregistry;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerregistryId;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/freelancerregistrys")
public class FreelancerregistryController {

  private final FreelancerregistryService service;

  public FreelancerregistryController(FreelancerregistryService service) {
    this.service = service;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public ResponseEntity<FreelancerregistryResponse> create(
      @Valid @RequestBody CreateFreelancerregistryRequest request) {
    Freelancerregistry entity =
        service.create(
            new CreateFreelancerregistryCommand(request.name(), request.description(), "system"));
    return ResponseEntity.created(URI.create("/api/v1/freelancerregistrys/" + entity.getId()))
        .body(FreelancerregistryResponse.from(entity));
  }

  @GetMapping("/{id}")
  public ResponseEntity<FreelancerregistryResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(
        FreelancerregistryResponse.from(service.getById(FreelancerregistryId.of(id))));
  }

  @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
  @PutMapping("/{id}")
  public ResponseEntity<FreelancerregistryResponse> update(
      @PathVariable String id, @Valid @RequestBody UpdateFreelancerregistryRequest request) {
    Freelancerregistry entity =
        service.update(
            FreelancerregistryId.of(id),
            new UpdateFreelancerregistryCommand(request.name(), request.description()));
    return ResponseEntity.ok(FreelancerregistryResponse.from(entity));
  }

  @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(FreelancerregistryId.of(id));
    return ResponseEntity.noContent().build();
  }
}
