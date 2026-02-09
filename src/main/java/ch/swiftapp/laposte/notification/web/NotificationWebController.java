package ch.swiftapp.laposte.notification.web;

import ch.swiftapp.laposte.branch.BranchModuleApi;
import ch.swiftapp.laposte.notification.dto.request.ComposeNotificationRequest;
import ch.swiftapp.laposte.notification.dto.response.InAppNotificationResponse;
import ch.swiftapp.laposte.notification.dto.response.NotificationLogResponse;
import ch.swiftapp.laposte.notification.enums.NotificationCategory;
import ch.swiftapp.laposte.notification.model.NotificationLog;
import ch.swiftapp.laposte.notification.repository.NotificationLogRepository;
import ch.swiftapp.laposte.notification.service.InAppNotificationService;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.user.UserModuleApi;
import ch.swiftapp.laposte.user.enums.EmployeeRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/notifications") @RequiredArgsConstructor
public class NotificationWebController {
    private final NotificationLogRepository logRepository;
    private final InAppNotificationService inAppService;
    private final UserModuleApi userModuleApi;
    private final BranchModuleApi branchModuleApi;

    // ── Email Notification Logs (admin only) ───────────────
    @GetMapping @PreAuthorize("hasRole('ADMIN')")
    public String list(Model model, @PageableDefault(size = 20) Pageable pageable) {
        Page<NotificationLog> page = logRepository.findAll(pageable);
        var content = page.getContent().stream().map(this::toLogResponse).toList();
        model.addAttribute("page", PagedResponse.of(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast()));
        return "notifications/list";
    }

    // ── In-App Notification Inbox ──────────────────────────
    @GetMapping("/inbox/{employeeId}")
    public String inbox(@PathVariable Long employeeId, Model model,
                        @PageableDefault(size = 20) Pageable pageable) {
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("page", inAppService.getForEmployee(employeeId, pageable));
        model.addAttribute("unreadCount", inAppService.countUnread(employeeId));
        return "notifications/inbox";
    }

    // ── HTMX: notification bell dropdown fragment ──────────
    @GetMapping("/bell/{employeeId}")
    public String bellDropdown(@PathVariable Long employeeId, Model model) {
        model.addAttribute("employeeId", employeeId);
        PagedResponse<InAppNotificationResponse> recent = inAppService.getForEmployee(
                employeeId, Pageable.ofSize(5));
        model.addAttribute("notifications", recent.getContent());
        model.addAttribute("unreadCount", inAppService.countUnread(employeeId));
        return "fragments/notification-bell :: bellDropdown";
    }

    // ── HTMX: unread count badge fragment ──────────────────
    @GetMapping("/badge/{employeeId}")
    public String badge(@PathVariable Long employeeId, Model model) {
        long count = inAppService.countUnread(employeeId);
        model.addAttribute("unreadCount", count);
        return "fragments/notification-bell :: badge";
    }

    // ── Mark single as read (HTMX POST) ───────────────────
    @PostMapping("/read/{notificationId}/{employeeId}")
    public String markAsRead(@PathVariable Long notificationId, @PathVariable Long employeeId,
                             RedirectAttributes flash) {
        inAppService.markAsRead(notificationId, employeeId);
        return "redirect:/notifications/inbox/" + employeeId;
    }

    // ── Mark all as read ──────────────────────────────────
    @PostMapping("/mark-all-read/{employeeId}")
    public String markAllAsRead(@PathVariable Long employeeId) {
        inAppService.markAllAsRead(employeeId);
        return "redirect:/notifications/inbox/" + employeeId;
    }

    // ── Compose In-App Notification ───────────────────────
    @GetMapping("/compose") @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
    public String composeForm(Model model) {
        model.addAttribute("categories", NotificationCategory.values());
        model.addAttribute("roles", EmployeeRole.values());
        model.addAttribute("branches", branchModuleApi.findAll());
        model.addAttribute("employees", userModuleApi.getAllEmployees(Pageable.ofSize(100)).getContent());
        return "notifications/compose";
    }

    @PostMapping("/compose") @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
    public String compose(@ModelAttribute ComposeNotificationRequest request,
                          @RequestParam(required = false) Long senderEmployeeId,
                          RedirectAttributes flash) {
        inAppService.compose(request, senderEmployeeId);
        flash.addFlashAttribute("successMessage", "Notification sent successfully!");
        return "redirect:/notifications/compose";
    }

    private NotificationLogResponse toLogResponse(NotificationLog log) {
        return NotificationLogResponse.builder()
                .id(log.getId()).recipientEmail(log.getRecipientEmail())
                .type(log.getType()).status(log.getStatus())
                .subject(log.getSubject()).referenceId(log.getReferenceId())
                .eventType(log.getEventType()).createdAt(log.getCreatedAt()).build();
    }
}
