package ch.swiftapp.laposte.notification.service;

import ch.swiftapp.laposte.notification.model.NotificationLog;
import ch.swiftapp.laposte.notification.model.NotificationTemplate;
import ch.swiftapp.laposte.notification.model.NotificationTemplateTranslation;
import ch.swiftapp.laposte.notification.enums.NotificationStatus;
import ch.swiftapp.laposte.notification.enums.NotificationType;
import ch.swiftapp.laposte.notification.repository.NotificationLogRepository;
import ch.swiftapp.laposte.notification.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j @Service @RequiredArgsConstructor
public class NotificationService {

    private final NotificationLogRepository logRepository;
    private final NotificationTemplateRepository templateRepository;
    private final EmailService emailService;

    /**
     * Process a domain event and send the appropriate notification.
     * Uses template + locale to resolve subject/body, then logs.
     */
    @Transactional
    public void processEvent(String eventType, String recipientEmail, String locale,
                             String referenceId, Map<String, String> placeholders) {
        templateRepository.findByTemplateCode(eventType).ifPresent(template -> {
            NotificationTemplateTranslation translation = template.getTranslation(locale);
            if (translation == null) {
                log.warn("No translation for template {} locale {}", template.getTemplateCode(), locale);
                return;
            }

            String subject = resolvePlaceholders(translation.getSubject(), placeholders);
            String body = resolvePlaceholders(translation.getBody(), placeholders);

            NotificationLog entry = NotificationLog.builder()
                    .recipientEmail(recipientEmail)
                    .type(NotificationType.EMAIL)
                    .status(NotificationStatus.PENDING)
                    .subject(subject)
                    .body(body)
                    .referenceId(referenceId)
                    .eventType(eventType)
                    .build();

            logRepository.save(entry);

            try {
                emailService.send(recipientEmail, subject, body);
                entry.setStatus(NotificationStatus.SENT);
                log.info("Notification sent: {} -> {} ({})", eventType, recipientEmail, locale);
            } catch (Exception e) {
                log.error("Failed to send email to {} for event {}: {}", recipientEmail, eventType, e.getMessage());
                entry.setStatus(NotificationStatus.FAILED);
            }
            logRepository.save(entry);
        });
    }

    private String resolvePlaceholders(String template, Map<String, String> placeholders) {
        String result = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }
}
