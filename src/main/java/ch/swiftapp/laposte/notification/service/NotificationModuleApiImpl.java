package ch.swiftapp.laposte.notification.service;

import ch.swiftapp.laposte.notification.NotificationModuleApi;
import ch.swiftapp.laposte.notification.dto.response.NotificationLogResponse;
import ch.swiftapp.laposte.notification.enums.NotificationStatus;
import ch.swiftapp.laposte.notification.model.NotificationLog;
import ch.swiftapp.laposte.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j @Service @RequiredArgsConstructor
public class NotificationModuleApiImpl implements NotificationModuleApi {

    private final NotificationLogRepository logRepository;
    private final EmailService emailService;

    @Override @Transactional(readOnly = true)
    public List<NotificationLogResponse> recentNotifications(int limit) {
        return logRepository.findTopNByOrderByCreatedAtDesc(limit)
                .stream()
                .map(entry -> NotificationLogResponse.builder()
                        .id(entry.getId())
                        .recipientEmail(entry.getRecipientEmail())
                        .type(entry.getType())
                        .status(entry.getStatus())
                        .subject(entry.getSubject())
                        .referenceId(entry.getReferenceId())
                        .eventType(entry.getEventType())
                        .createdAt(entry.getCreatedAt())
                        .build())
                .toList();
    }

    @Override @Transactional(readOnly = true)
    public long countAll() {
        return logRepository.count();
    }

    @Override @Transactional
    public int retryFailedNotifications() {
        List<NotificationLog> failed = logRepository.findByStatus(NotificationStatus.FAILED);
        int retried = 0;
        for (NotificationLog entry : failed) {
            try {
                emailService.send(entry.getRecipientEmail(), entry.getSubject(), entry.getBody());
                entry.setStatus(NotificationStatus.SENT);
                logRepository.save(entry);
                retried++;
            } catch (Exception e) {
                log.warn("Retry failed for notification #{}: {}", entry.getId(), e.getMessage());
            }
        }
        return retried;
    }
}

