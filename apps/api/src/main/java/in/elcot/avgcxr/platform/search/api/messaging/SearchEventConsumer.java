package in.elcot.avgcxr.platform.search.api.messaging;

import in.elcot.avgcxr.platform.search.domain.event.SearchCreatedEvent;
import in.elcot.avgcxr.platform.search.domain.event.SearchUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SearchEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(SearchEventConsumer.class);

    @RabbitListener(queues = "avgc.search.created")
    public void onCreated(SearchCreatedEvent event) {
        log.info("Search created event: id={}", event.searchId());
    }

    @RabbitListener(queues = "avgc.search.updated")
    public void onUpdated(SearchUpdatedEvent event) {
        log.info("Search updated event: id={}, field={}", event.searchId(), event.field());
    }
}
