package ch.swiftapp.laposte.notification.model;

import ch.swiftapp.laposte.notification.enums.NotificationStatus;
import ch.swiftapp.laposte.notification.enums.NotificationType;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity @Table(name = "notification_log")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class NotificationLog extends BaseEntity {

    @Column(name = "recipient_email", length = 150) private String recipientEmail;
    @Column(name = "recipient_phone", length = 30) private String recipientPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20) private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20) private NotificationStatus status;

    @Column(name = "subject") private String subject;
    @Column(name = "body", columnDefinition = "TEXT") private String body;
    @Column(name = "reference_id", length = 50) private String referenceId;
    @Column(name = "event_type", length = 50) private String eventType;

    @Column(name = "retry_count", nullable = false)
    @Builder.Default private Integer retryCount = 0;
}

