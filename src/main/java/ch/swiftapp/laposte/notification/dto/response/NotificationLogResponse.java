package ch.swiftapp.laposte.notification.dto.response;

import ch.swiftapp.laposte.notification.enums.NotificationStatus;
import ch.swiftapp.laposte.notification.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NotificationLogResponse {
    private Long id;
    private String recipientEmail;
    private NotificationType type;
    private NotificationStatus status;
    private String subject;
    private String referenceId;
    private String eventType;
    private LocalDateTime createdAt;
}

