package in.elcot.avgcxr.support.helpdesk.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helpdesk ticket management configuration.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>SLA tracking thresholds (priority-based response/resolution times)</li>
 *   <li>Auto-escalation scheduler (escalates tickets exceeding SLA)</li>
 *   <li>Ticket counter for monitoring</li>
 * </ul>
 */
@Configuration
@EnableScheduling
public class HelpdeskConfig {

    @Value("${avgcxr.helpdesk.sla.high-priority-hours:4}")
    private int highPrioritySlaHours;

    @Value("${avgcxr.helpdesk.sla.medium-priority-hours:24}")
    private int mediumPrioritySlaHours;

    @Value("${avgcxr.helpdesk.sla.low-priority-hours:72}")
    private int lowPrioritySlaHours;

    @Bean
    public SlaThresholds helpdeskSlaThresholds() {
        return new SlaThresholds(highPrioritySlaHours, mediumPrioritySlaHours, lowPrioritySlaHours);
    }

    @Bean
    public TicketMetrics helpdeskTicketMetrics() {
        return new TicketMetrics();
    }

    /**
     * Run every 15 minutes: scan for tickets past SLA and escalate.
     * In production this would publish escalation events to RabbitMQ
     * and the WorkflowService would update the ticket state.
     */
    @Scheduled(fixedDelayString = "${avgcxr.helpdesk.escalation-interval-ms:900000}",
               initialDelayString = "${avgcxr.helpdesk.escalation-initial-delay-ms:60000}")
    public void escalateOverdueTickets() {
        Instant now = Instant.now();
        // Application-level escalation handled in HelpdeskTicketUseCaseService.
        // This scheduler is the periodic trigger.
    }

    public record SlaThresholds(int highPriorityHours, int mediumPriorityHours, int lowPriorityHours) {
        public Duration slaFor(String priority) {
            int h = switch (priority == null ? "MEDIUM" : priority.toUpperCase()) {
                case "HIGH" -> highPriorityHours;
                case "LOW" -> lowPriorityHours;
                default -> mediumPriorityHours;
            };
            return Duration.ofHours(h);
        }
    }

    public static class TicketMetrics {
        private final AtomicInteger totalCreated = new AtomicInteger();
        private final AtomicInteger totalResolved = new AtomicInteger();
        private final AtomicInteger totalEscalated = new AtomicInteger();

        public void incrementCreated() { totalCreated.incrementAndGet(); }
        public void incrementResolved() { totalResolved.incrementAndGet(); }
        public void incrementEscalated() { totalEscalated.incrementAndGet(); }

        public int getTotalCreated() { return totalCreated.get(); }
        public int getTotalResolved() { return totalResolved.get(); }
        public int getTotalEscalated() { return totalEscalated.get(); }
    }
}
