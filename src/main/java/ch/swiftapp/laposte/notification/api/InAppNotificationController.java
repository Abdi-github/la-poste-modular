package ch.swiftapp.laposte.notification.api;

import ch.swiftapp.laposte.notification.dto.request.ComposeNotificationRequest;
import ch.swiftapp.laposte.notification.dto.response.InAppNotificationResponse;
import ch.swiftapp.laposte.notification.service.InAppNotificationService;
import ch.swiftapp.laposte.shared.dto.ApiResponse;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static ch.swiftapp.laposte.shared.constants.ApiPaths.NOTIFICATIONS;

@RestController @RequestMapping(NOTIFICATIONS + "/in-app") @RequiredArgsConstructor
@Tag(name = "In-App Notifications", description = "In-app notification management for employees")
public class InAppNotificationController {

    private final InAppNotificationService inAppService;

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get notifications for an employee")
    public ResponseEntity<PagedResponse<InAppNotificationResponse>> getForEmployee(
            @PathVariable Long employeeId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(inAppService.getForEmployee(employeeId, pageable));
    }

    @GetMapping("/employee/{employeeId}/unread-count")
    @Operation(summary = "Get unread notification count for an employee")
    public ResponseEntity<ApiResponse<Map<String, Long>>> unreadCount(@PathVariable Long employeeId) {
        long count = inAppService.countUnread(employeeId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("count", count)));
    }

    @PostMapping("/{notificationId}/read/{employeeId}")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId, @PathVariable Long employeeId) {
        inAppService.markAsRead(notificationId, employeeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-all-read/{employeeId}")
    @Operation(summary = "Mark all notifications as read for an employee")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long employeeId) {
        inAppService.markAllAsRead(employeeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
    @Operation(summary = "Compose and send a new in-app notification")
    public ResponseEntity<ApiResponse<InAppNotificationResponse>> compose(
            @Valid @RequestBody ComposeNotificationRequest request,
            @RequestParam(required = false) Long senderEmployeeId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(inAppService.compose(request, senderEmployeeId)));
    }
}

