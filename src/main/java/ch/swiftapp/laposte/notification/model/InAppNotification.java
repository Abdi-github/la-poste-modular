package ch.swiftapp.laposte.notification.model;

import ch.swiftapp.laposte.notification.enums.NotificationCategory;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "in_app_notification", indexes = {
        @Index(name = "idx_inapp_target_employee", columnList = "target_employee_id"),
        @Index(name = "idx_inapp_target_role", columnList = "target_role"),
        @Index(name = "idx_inapp_target_branch", columnList = "target_branch_id"),
        @Index(name = "idx_inapp_created_at", columnList = "created_at")
})
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class InAppNotification extends BaseEntity {

    // ── Targeting (NULL = broadcast) ──────────────────
    @Column(name = "target_employee_id") private Long targetEmployeeId;
    @Column(name = "target_role", length = 30) private String targetRole;
    @Column(name = "target_branch_id") private Long targetBranchId;

    // ── Content ──────────────────────────────────────
    @Column(name = "title", nullable = false) private String title;
    @Column(name = "message", nullable = false, columnDefinition = "TEXT") private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    @Builder.Default private NotificationCategory category = NotificationCategory.INFO;

    @Column(name = "reference_url", length = 500) private String referenceUrl;

    // ── Sender ───────────────────────────────────────
    @Column(name = "sender_employee_id") private Long senderEmployeeId;

    // ── Read receipts ────────────────────────────────
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default private Set<InAppNotificationRead> readReceipts = new HashSet<>();

    /**
     * Check if a specific employee has read this notification.
     */
    public boolean isReadBy(Long employeeId) {
        return readReceipts.stream().anyMatch(r -> r.getEmployeeId().equals(employeeId));
    }
}

