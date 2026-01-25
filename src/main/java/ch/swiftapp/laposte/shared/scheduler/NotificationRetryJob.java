package ch.swiftapp.laposte.shared.scheduler;

import ch.swiftapp.laposte.notification.NotificationModuleApi;
import ch.swiftapp.laposte.shared.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Retries failed email notifications (every 15 minutes).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRetryJob {

    private final NotificationModuleApi notificationModuleApi;
    private final AuditService auditService;

    @Scheduled(fixedDelayString = "${app.scheduler.notification-retry-ms:900000}") // 15 min
    public void retryFailedNotifications() {
        log.debug("Running notification retry job...");
        int retried = notificationModuleApi.retryFailedNotifications();
        if (retried > 0) {
            log.info("Retried {} failed notifications", retried);
            auditService.log("System", 0L, "SCHEDULED_JOB", "NotificationRetry: retried " + retried);
        }
    }
}

