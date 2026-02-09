package ch.swiftapp.laposte.notification.dto.request;

import ch.swiftapp.laposte.notification.enums.NotificationCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ComposeNotificationRequest {

    @NotBlank @Size(max = 255)
    private String title;

    @NotBlank
    private String message;

    @NotNull
    @Builder.Default private NotificationCategory category = NotificationCategory.INFO;

    /** Optional deep link, e.g. /parcels */
    private String referenceUrl;

    // ── Targeting (all null = broadcast to everyone) ──
    /** Send to a specific employee by ID */
    private Long targetEmployeeId;

    /** Send to all employees with this role: ADMIN, BRANCH_MANAGER, EMPLOYEE */
    private String targetRole;

    /** Send to all employees at this branch */
    private Long targetBranchId;
}

