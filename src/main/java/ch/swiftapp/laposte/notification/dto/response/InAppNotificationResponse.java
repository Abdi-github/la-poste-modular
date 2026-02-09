package ch.swiftapp.laposte.notification.dto.response;

import ch.swiftapp.laposte.notification.enums.NotificationCategory;
import lombok.*;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class InAppNotificationResponse {
    private Long id;
    private String title;
    private String message;
    private NotificationCategory category;
    private String referenceUrl;
    private String targetRole;
    private Long targetBranchId;
    private Long targetEmployeeId;
    private Long senderEmployeeId;
    private String senderName;
    private boolean read;
    private LocalDateTime createdAt;
}

