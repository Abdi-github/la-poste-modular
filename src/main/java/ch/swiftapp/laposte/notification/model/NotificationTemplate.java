package ch.swiftapp.laposte.notification.model;

import ch.swiftapp.laposte.notification.enums.NotificationType;
import ch.swiftapp.laposte.shared.i18n.TranslatableEntity;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "notification_template")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class NotificationTemplate extends BaseEntity implements TranslatableEntity<NotificationTemplateTranslation> {

    @Column(name = "template_code", nullable = false, unique = true, length = 50) private String templateCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20) private NotificationType type;

    @Column(name = "event_type", nullable = false, length = 50) private String eventType;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default private Set<NotificationTemplateTranslation> translations = new HashSet<>();
}

