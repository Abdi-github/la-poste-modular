package ch.swiftapp.laposte.notification.repository;

import ch.swiftapp.laposte.notification.model.InAppNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InAppNotificationRepository extends JpaRepository<InAppNotification, Long> {

    /**
     * Find notifications visible to a specific employee.
     * A notification is visible if:
     *   - It targets this employee specifically, OR
     *   - It targets the employee's role (and not a different individual), OR
     *   - It targets the employee's branch (and not a different individual), OR
     *   - It is a broadcast (all target fields are NULL)
     */
    @Query("""
        SELECT n FROM InAppNotification n
        WHERE (n.targetEmployeeId = :employeeId)
           OR (n.targetEmployeeId IS NULL AND n.targetRole = :role AND (n.targetBranchId IS NULL OR n.targetBranchId = :branchId))
           OR (n.targetEmployeeId IS NULL AND n.targetRole IS NULL AND n.targetBranchId = :branchId)
           OR (n.targetEmployeeId IS NULL AND n.targetRole IS NULL AND n.targetBranchId IS NULL)
        ORDER BY n.createdAt DESC
    """)
    Page<InAppNotification> findVisibleForEmployee(
            @Param("employeeId") Long employeeId,
            @Param("role") String role,
            @Param("branchId") Long branchId,
            Pageable pageable);

    /**
     * Count unread notifications for a specific employee.
     */
    @Query("""
        SELECT COUNT(n) FROM InAppNotification n
        WHERE ((n.targetEmployeeId = :employeeId)
           OR (n.targetEmployeeId IS NULL AND n.targetRole = :role AND (n.targetBranchId IS NULL OR n.targetBranchId = :branchId))
           OR (n.targetEmployeeId IS NULL AND n.targetRole IS NULL AND n.targetBranchId = :branchId)
           OR (n.targetEmployeeId IS NULL AND n.targetRole IS NULL AND n.targetBranchId IS NULL))
          AND NOT EXISTS (SELECT r FROM InAppNotificationRead r WHERE r.notification = n AND r.employeeId = :employeeId)
    """)
    long countUnreadForEmployee(
            @Param("employeeId") Long employeeId,
            @Param("role") String role,
            @Param("branchId") Long branchId);
}

