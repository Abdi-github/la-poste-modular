package ch.swiftapp.laposte.notification.model;

import ch.swiftapp.laposte.shared.i18n.BaseTranslation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "notification_template_translation",
       uniqueConstraints = @UniqueConstraint(columnNames = {"template_id", "locale"}))
@Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
public class NotificationTemplateTranslation extends BaseTranslation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false) private NotificationTemplate template;

    @Column(name = "subject", nullable = false) private String subject;
    @Column(name = "body", nullable = false, columnDefinition = "TEXT") private String body;
}

