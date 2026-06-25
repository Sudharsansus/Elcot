package in.elcot.avgcxr.ecosystem.talentconnect.api.rest;

import in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.request.CreateTalentconnectRequest;
import in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.request.UpdateTalentconnectRequest;
import in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.response.TalentconnectResponse;
import in.elcot.avgcxr.ecosystem.talentconnect.application.command.CreateTalentconnectCommand;
import in.elcot.avgcxr.ecosystem.talentconnect.application.command.UpdateTalentconnectCommand;
import in.elcot.avgcxr.ecosystem.talentconnect.application.service.TalentconnectService;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.Talentconnect;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentconnectId;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/talentconnects")
public class TalentconnectController {

  private final TalentconnectService service;

  public TalentconnectController(TalentconnectService service) {
    this.service = service;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public ResponseEntity<TalentconnectResponse> create(
      @Valid @RequestBody CreateTalentconnectRequest request) {
    Talentconnect entity =
        service.create(
            new CreateTalentconnectCommand(request.name(), request.description(), "system"));
    return ResponseEntity.created(URI.create("/api/v1/talentconnects/" + entity.getId()))
        .body(TalentconnectResponse.from(entity));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TalentconnectResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(TalentconnectResponse.from(service.getById(TalentconnectId.of(id))));
  }

  @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
  @PutMapping("/{id}")
  public ResponseEntity<TalentconnectResponse> update(
      @PathVariable String id, @Valid @RequestBody UpdateTalentconnectRequest request) {
    Talentconnect entity =
        service.update(
            TalentconnectId.of(id),
            new UpdateTalentconnectCommand(request.name(), request.description()));
    return ResponseEntity.ok(TalentconnectResponse.from(entity));
  }

  @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(TalentconnectId.of(id));
    return ResponseEntity.noContent().build();
  }
}
