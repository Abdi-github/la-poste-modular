package ch.swiftapp.laposte.notification.model;

import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity @Table(name = "notification_preference")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class NotificationPreference extends BaseEntity {

    @Column(name = "customer_id", nullable = false, unique = true) private Long customerId;

    @Column(name = "email_enabled", nullable = false)
    @Builder.Default private Boolean emailEnabled = true;

    @Column(name = "sms_enabled", nullable = false)
    @Builder.Default private Boolean smsEnabled = false;

    @Column(name = "in_app_enabled", nullable = false)
    @Builder.Default private Boolean inAppEnabled = true;

    @Column(name = "preferred_locale", nullable = false, length = 5)
    @Builder.Default private String preferredLocale = "de";
}

