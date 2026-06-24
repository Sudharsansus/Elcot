package in.elcot.avgcxr.platform.file.api.rest;

import in.elcot.avgcxr.platform.file.api.rest.dto.response.FileResponse;
import in.elcot.avgcxr.platform.file.application.command.CreateFileCommand;
import in.elcot.avgcxr.platform.file.application.service.FileService;
import in.elcot.avgcxr.platform.file.domain.model.File;
import in.elcot.avgcxr.platform.file.domain.model.FileId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) { this.fileService = fileService; }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public ResponseEntity<FileResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("entityType") String entityType,
            @RequestParam("entityId") String entityId,
            Principal principal) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String uploader = principal != null ? principal.getName() : "anonymous";
        CreateFileCommand cmd = new CreateFileCommand(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                uploader,
                entityType,
                entityId,
                file.getBytes()   // real bytes go to MinIO via the storage port
        );
        File uploaded = fileService.create(cmd);
        return ResponseEntity.created(URI.create("/api/v1/files/" + uploaded.getId()))
                .body(FileResponse.from(uploaded));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(FileResponse.from(fileService.getById(FileId.of(id))));
    }
}
