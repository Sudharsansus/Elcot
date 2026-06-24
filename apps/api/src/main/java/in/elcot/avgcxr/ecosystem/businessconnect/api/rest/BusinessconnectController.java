package in.elcot.avgcxr.ecosystem.businessconnect.api.rest;

import in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.request.CreateBusinessconnectRequest;
import in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.request.UpdateBusinessconnectRequest;
import in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.response.BusinessconnectResponse;
import in.elcot.avgcxr.ecosystem.businessconnect.application.command.CreateBusinessconnectCommand;
import in.elcot.avgcxr.ecosystem.businessconnect.application.command.UpdateBusinessconnectCommand;
import in.elcot.avgcxr.ecosystem.businessconnect.application.service.BusinessconnectService;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Businessconnect;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.BusinessconnectId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/businessconnects")
public class BusinessconnectController {

    private final BusinessconnectService service;

    public BusinessconnectController(BusinessconnectService service) { this.service = service; }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<BusinessconnectResponse> create(@Valid @RequestBody CreateBusinessconnectRequest request) {
        Businessconnect entity = service.create(new CreateBusinessconnectCommand(request.name(), request.description(), "system"));
        return ResponseEntity.created(URI.create("/api/v1/businessconnects/" + entity.getId()))
            .body(BusinessconnectResponse.from(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessconnectResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(BusinessconnectResponse.from(service.getById(BusinessconnectId.of(id))));
    }

    @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
    @PutMapping("/{id}")
    public ResponseEntity<BusinessconnectResponse> update(@PathVariable String id, @Valid @RequestBody UpdateBusinessconnectRequest request) {
        Businessconnect entity = service.update(BusinessconnectId.of(id), new UpdateBusinessconnectCommand(request.name(), request.description()));
        return ResponseEntity.ok(BusinessconnectResponse.from(entity));
    }

    @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(BusinessconnectId.of(id));
        return ResponseEntity.noContent().build();
    }
}
