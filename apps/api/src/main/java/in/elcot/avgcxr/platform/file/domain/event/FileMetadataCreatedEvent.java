package in.elcot.avgcxr.platform.file.domain.event;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class FileMetadataCreatedEvent extends DomainEvent {
    public FileMetadataCreatedEvent(UUID entityId) {
        super(UUID.randomUUID(), "FILEMETADATA_CREATED", LocalDateTime.now(), entityId.toString(),
                Map.of("entityId", entityId.toString()));
    }
}
