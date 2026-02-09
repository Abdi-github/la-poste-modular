package ch.swiftapp.laposte.notification.repository;

import ch.swiftapp.laposte.notification.model.NotificationLog;
import ch.swiftapp.laposte.notification.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    Page<NotificationLog> findByReferenceId(String referenceId, Pageable pageable);
    List<NotificationLog> findByStatus(NotificationStatus status);

    /**
     * Find most recent notification logs, ordered by createdAt descending.
     */
    default List<NotificationLog> findTopNByOrderByCreatedAtDesc(int limit) {
        return findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"))).getContent();
    }
}

