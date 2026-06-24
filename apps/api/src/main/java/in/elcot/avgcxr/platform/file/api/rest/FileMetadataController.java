package in.elcot.avgcxr.platform.file.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.file.api.rest.dto.response.FileMetadataResponse;
import in.elcot.avgcxr.platform.file.application.port.input.GetFileMetadataUseCase;
import in.elcot.avgcxr.platform.file.application.port.input.CreateFileMetadataUseCase;
import in.elcot.avgcxr.platform.file.application.command.CreateFileMetadataCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for File upload, download, and management via MinIO S3-compatible storage with presigned URLs.
 *
 * Provides CRUD operations and domain-specific actions
 * following the hexagonal architecture pattern.
 */
@RestController
@RequestMapping("/api/v1/file-metadata")
@RequiredArgsConstructor
public class FileMetadataController {
    private final CreateFileMetadataUseCase createFileMetadataUseCase;
    private final GetFileMetadataUseCase getFileMetadataUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FileMetadataResponse>>> list(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<FileMetadataResponse> results = getFileMetadataUseCase.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.paginated(
                results.getContent(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FileMetadataResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(getFileMetadataUseCase.getById(id)));
    }

    @PreAuthorize("hasAnyRole('ADMIN','NODAL_OFFICER')")
    @PostMapping
    public ResponseEntity<ApiResponse<FileMetadataResponse>> create(
            @Valid @RequestBody CreateFileMetadataCommand command) {
        return ResponseEntity.ok(ApiResponse.success(createFileMetadataUseCase.create(command)));
    }
}
