package ch.swiftapp.laposte.notification.repository;

import ch.swiftapp.laposte.notification.model.InAppNotificationRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InAppNotificationReadRepository extends JpaRepository<InAppNotificationRead, Long> {
    Optional<InAppNotificationRead> findByNotificationIdAndEmployeeId(Long notificationId, Long employeeId);
    boolean existsByNotificationIdAndEmployeeId(Long notificationId, Long employeeId);
}

