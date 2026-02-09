package ch.swiftapp.laposte.notification.repository;

import ch.swiftapp.laposte.notification.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByEventType(String eventType);
    Optional<NotificationTemplate> findByTemplateCode(String templateCode);
}

