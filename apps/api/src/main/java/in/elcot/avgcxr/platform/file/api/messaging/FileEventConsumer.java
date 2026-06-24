package in.elcot.avgcxr.platform.file.api.messaging;

import in.elcot.avgcxr.platform.file.domain.event.FileCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FileEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(FileEventConsumer.class);

    @RabbitListener(queues = "avgc.file.created")
    public void onFileCreated(FileCreatedEvent event) {
        log.info("File uploaded: id={}, name={}, size={}", event.fileId(), event.fileName(), event.fileSize());
    }
}

