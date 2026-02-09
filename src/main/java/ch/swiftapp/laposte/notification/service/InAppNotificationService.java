package ch.swiftapp.laposte.notification.service;

import ch.swiftapp.laposte.notification.dto.request.ComposeNotificationRequest;
import ch.swiftapp.laposte.notification.dto.response.InAppNotificationResponse;
import ch.swiftapp.laposte.notification.model.InAppNotification;
import ch.swiftapp.laposte.notification.model.InAppNotificationRead;
import ch.swiftapp.laposte.notification.repository.InAppNotificationReadRepository;
import ch.swiftapp.laposte.notification.repository.InAppNotificationRepository;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.user.UserModuleApi;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j @Service @RequiredArgsConstructor
public class InAppNotificationService {

    private final InAppNotificationRepository notificationRepository;
    private final InAppNotificationReadRepository readRepository;
    private final UserModuleApi userModuleApi;

    /**
     * Create a new in-app notification (individual, role-based, branch-based, or broadcast).
     */
    @Transactional
    public InAppNotificationResponse compose(ComposeNotificationRequest request, Long senderEmployeeId) {
        
        InAppNotification notification = InAppNotification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .category(request.getCategory())
                .referenceUrl(request.getReferenceUrl())
                .targetEmployeeId(request.getTargetEmployeeId())
                .targetRole(request.getTargetRole())
                .targetBranchId(request.getTargetBranchId())
                .senderEmployeeId(senderEmployeeId)
                .build();

        InAppNotification saved = notificationRepository.save(notification);
        
        log.info("In-app notification created: id={} title='{}' target=[emp={}, role={}, branch={}]",
                saved.getId(), saved.getTitle(),
                saved.getTargetEmployeeId(), saved.getTargetRole(), saved.getTargetBranchId());

        return toResponse(saved, null);
    }

    /**
     * Get notifications visible to a specific employee, paged.
     */
    @Transactional(readOnly = true)
    public PagedResponse<InAppNotificationResponse> getForEmployee(Long employeeId, Pageable pageable) {
        EmployeeResponse employee = userModuleApi.findEmployeeById(employeeId).orElse(null);
        if (employee == null) return PagedResponse.of(java.util.List.of(), 0, pageable.getPageSize(), 0, 0, true);

        String role = employee.getRole() != null ? employee.getRole().name() : null;
        Long branchId = employee.getAssignedBranchId();

        Page<InAppNotification> page = notificationRepository.findVisibleForEmployee(
                employeeId, role, branchId, pageable);

        var content = page.getContent().stream()
                .map(n -> toResponse(n, employeeId))
                .toList();

        return PagedResponse.of(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    /**
     * Count unread notifications for an employee.
     */
    @Transactional(readOnly = true)
    public long countUnread(Long employeeId) {
        EmployeeResponse employee = userModuleApi.findEmployeeById(employeeId).orElse(null);
        if (employee == null) return 0;

        String role = employee.getRole() != null ? employee.getRole().name() : null;
        Long branchId = employee.getAssignedBranchId();

        return notificationRepository.countUnreadForEmployee(employeeId, role, branchId);
    }

    /**
     * Mark a notification as read by an employee.
     */
    @Transactional
    public void markAsRead(Long notificationId, Long employeeId) {
        
        if (readRepository.existsByNotificationIdAndEmployeeId(notificationId, employeeId)) {
            return; // already read
        }
        Optional<InAppNotification> notifOpt = notificationRepository.findById(notificationId);
        if (notifOpt.isEmpty()) {
            return;
        }

        InAppNotificationRead read = InAppNotificationRead.builder()
                .notification(notifOpt.get())
                .employeeId(employeeId)
                .readAt(LocalDateTime.now())
                .build();
        
        readRepository.save(read);
        
        log.debug("Notification {} marked as read by employee {}", notificationId, employeeId);
    }

    /**
     * Mark all visible notifications as read for an employee.
     */
    @Transactional
    public void markAllAsRead(Long employeeId) {
        EmployeeResponse employee = userModuleApi.findEmployeeById(employeeId).orElse(null);
        if (employee == null) return;

        String role = employee.getRole() != null ? employee.getRole().name() : null;
        Long branchId = employee.getAssignedBranchId();

        Page<InAppNotification> page = notificationRepository.findVisibleForEmployee(
                employeeId, role, branchId, Pageable.unpaged());

        for (InAppNotification n : page.getContent()) {
            if (!readRepository.existsByNotificationIdAndEmployeeId(n.getId(), employeeId)) {
                readRepository.save(InAppNotificationRead.builder()
                        .notification(n)
                        .employeeId(employeeId)
                        .readAt(LocalDateTime.now())
                        .build());
            }
        }
        log.info("Marked all notifications as read for employee {}", employeeId);
    }

    private InAppNotificationResponse toResponse(InAppNotification n, Long viewerEmployeeId) {
        boolean isRead = viewerEmployeeId != null && n.isReadBy(viewerEmployeeId);
        String senderName = null;
        if (n.getSenderEmployeeId() != null) {
            senderName = userModuleApi.findEmployeeById(n.getSenderEmployeeId())
                    .map(e -> e.getFirstName() + " " + e.getLastName())
                    .orElse("System");
        }

        return InAppNotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .category(n.getCategory())
                .referenceUrl(n.getReferenceUrl())
                .targetRole(n.getTargetRole())
                .targetBranchId(n.getTargetBranchId())
                .targetEmployeeId(n.getTargetEmployeeId())
                .senderEmployeeId(n.getSenderEmployeeId())
                .senderName(senderName)
                .read(isRead)
                .createdAt(n.getCreatedAt())
                .build();
    }
}

