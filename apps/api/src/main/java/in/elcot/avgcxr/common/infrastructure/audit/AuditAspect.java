package in.elcot.avgcxr.common.infrastructure.audit;

import in.elcot.avgcxr.common.infrastructure.messaging.RabbitMQProducer;
import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Aspect
@org.springframework.context.annotation.Profile("!no-rabbit")
@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class AuditAspect {

    private final RabbitMQProducer eventPublisher;

    @Around("@annotation(in.elcot.avgcxr.platformcore.audit.Auditable)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        String action = joinPoint.getSignature().getName();
        String actor = getCurrentUserId();
        String ipAddress = getCurrentIpAddress();

        log.debug("Audit: action={}, actor={}, ip={}", action, actor, ipAddress);

        Object result = joinPoint.proceed();

        DomainEvent auditEvent = new DomainEvent(
                UUID.randomUUID(), "AUDIT." + action.toUpperCase(),
                LocalDateTime.now(), (String) null, Map.of(
                        "action", action,
                        "actor", actor != null ? actor : "ANONYMOUS",
                        "ipAddress", ipAddress != null ? ipAddress : "UNKNOWN",
                        "method", joinPoint.getSignature().getDeclaringTypeName()
                )
        );
        eventPublisher.publish(auditEvent, in.elcot.avgcxr.common.infrastructure.config.RabbitMQConfig.RK_AUDIT_CREATED);

        return result;
    }

    private String getCurrentUserId() {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes sra) {
            var auth = sra.getRequest().getUserPrincipal();
            return auth != null ? auth.getName() : null;
        }
        return null;
    }

    private String getCurrentIpAddress() {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes sra) {
            HttpServletRequest request = sra.getRequest();
            String xff = request.getHeader("X-Forwarded-For");
            return (xff != null && !xff.isEmpty()) ? xff.split(",")[0].trim() : request.getRemoteAddr();
        }
        return null;
    }
}
