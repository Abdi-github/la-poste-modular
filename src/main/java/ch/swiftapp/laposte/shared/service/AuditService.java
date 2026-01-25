package ch.swiftapp.laposte.shared.service;

import ch.swiftapp.laposte.shared.model.AuditLog;
import ch.swiftapp.laposte.shared.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for recording audit trail entries.
 * Uses @Async to avoid blocking the main transaction.
 */
@Slf4j @Service @RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Record an audit log entry asynchronously.
     */
    @Async
    public void log(String entityType, Long entityId, String action, String details) {
        String performedBy = resolveCurrentUser();
        AuditLog entry = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .performedBy(performedBy)
                .details(details)
                .build();
        auditLogRepository.save(entry);
        log.debug("Audit: {} {} {} by {} — {}", action, entityType, entityId, performedBy, details);
    }

    /**
     * Record an audit log entry with a known performer.
     */
    @Async
    public void log(String entityType, Long entityId, String action, String performedBy, String details) {
        AuditLog entry = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .performedBy(performedBy)
                .details(details)
                .build();
        auditLogRepository.save(entry);
        log.debug("Audit: {} {} {} by {} — {}", action, entityType, entityId, performedBy, details);
    }

    public List<AuditLog> recentEntries() {
        return auditLogRepository.findTop20ByOrderByCreatedAtDesc();
    }

    private String resolveCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "system";
        }
        return auth.getName();
    }
}

