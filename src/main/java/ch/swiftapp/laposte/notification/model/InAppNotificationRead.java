package ch.swiftapp.laposte.notification.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "in_app_notification_read",
        uniqueConstraints = @UniqueConstraint(columnNames = {"notification_id", "employee_id"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class InAppNotificationRead {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private InAppNotification notification;

    @Column(name = "employee_id", nullable = false) private Long employeeId;

    @Column(name = "read_at", nullable = false)
    @Builder.Default private LocalDateTime readAt = LocalDateTime.now();
}

