package in.elcot.avgcxr.support.helpdesk.api.rest;

import in.elcot.avgcxr.support.helpdesk.api.rest.dto.request.CreateHelpdeskRequest;
import in.elcot.avgcxr.support.helpdesk.api.rest.dto.request.UpdateHelpdeskRequest;
import in.elcot.avgcxr.support.helpdesk.api.rest.dto.response.HelpdeskResponse;
import in.elcot.avgcxr.support.helpdesk.application.command.CreateHelpdeskCommand;
import in.elcot.avgcxr.support.helpdesk.application.command.UpdateHelpdeskCommand;
import in.elcot.avgcxr.support.helpdesk.application.service.HelpdeskService;
import in.elcot.avgcxr.support.helpdesk.domain.model.Helpdesk;
import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/helpdesks")
public class HelpdeskController {

    private final HelpdeskService service;

    public HelpdeskController(HelpdeskService service) { this.service = service; }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<HelpdeskResponse> create(@Valid @RequestBody CreateHelpdeskRequest request) {
        Helpdesk entity = service.create(new CreateHelpdeskCommand(request.name(), request.description(), "system"));
        return ResponseEntity.created(URI.create("/api/v1/helpdesks/" + entity.getId()))
            .body(HelpdeskResponse.from(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpdeskResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(HelpdeskResponse.from(service.getById(HelpdeskId.of(id))));
    }

    @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
    @PutMapping("/{id}")
    public ResponseEntity<HelpdeskResponse> update(@PathVariable String id, @Valid @RequestBody UpdateHelpdeskRequest request) {
        Helpdesk entity = service.update(HelpdeskId.of(id), new UpdateHelpdeskCommand(request.name(), request.description()));
        return ResponseEntity.ok(HelpdeskResponse.from(entity));
    }

    @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(HelpdeskId.of(id));
        return ResponseEntity.noContent().build();
    }
}
