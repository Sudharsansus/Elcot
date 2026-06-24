package in.elcot.avgcxr.platform.audit.api.rest;

import in.elcot.avgcxr.platform.audit.api.rest.dto.request.CreateAuditRequest;
import in.elcot.avgcxr.platform.audit.api.rest.dto.request.UpdateAuditRequest;
import in.elcot.avgcxr.platform.audit.api.rest.dto.response.AuditResponse;
import in.elcot.avgcxr.platform.audit.application.command.CreateAuditCommand;
import in.elcot.avgcxr.platform.audit.application.command.UpdateAuditCommand;
import in.elcot.avgcxr.platform.audit.application.service.AuditService;
import in.elcot.avgcxr.platform.audit.domain.model.Audit;
import in.elcot.avgcxr.platform.audit.domain.model.AuditId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/audits")
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) { this.service = service; }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AuditResponse> create(@Valid @RequestBody CreateAuditRequest request) {
        Audit entity = service.create(new CreateAuditCommand(request.name(), request.description(), "system"));
        return ResponseEntity.created(URI.create("/api/v1/audits/" + entity.getId()))
            .body(AuditResponse.from(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(AuditResponse.from(service.getById(AuditId.of(id))));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AuditResponse> update(@PathVariable String id, @Valid @RequestBody UpdateAuditRequest request) {
        Audit entity = service.update(AuditId.of(id), new UpdateAuditCommand(request.name(), request.description()));
        return ResponseEntity.ok(AuditResponse.from(entity));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(AuditId.of(id));
        return ResponseEntity.noContent().build();
    }
}
