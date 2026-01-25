package ch.swiftapp.laposte.shared.web;

import ch.swiftapp.laposte.shared.model.AuditLog;
import ch.swiftapp.laposte.shared.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Admin page for viewing the audit trail.
 */
@Controller
@RequestMapping("/admin/audit-log")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AuditLogWebController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String entityType,
                       @RequestParam(required = false) String action,
                       @PageableDefault(size = 30) Pageable pageable) {
        Page<AuditLog> page;
        if (entityType != null && !entityType.isBlank() && action != null && !action.isBlank()) {
            page = auditLogRepository.findByEntityTypeAndActionOrderByCreatedAtDesc(entityType, action, pageable);
        } else if (entityType != null && !entityType.isBlank()) {
            page = auditLogRepository.findByEntityTypeOrderByCreatedAtDesc(entityType, pageable);
        } else if (action != null && !action.isBlank()) {
            page = auditLogRepository.findByActionOrderByCreatedAtDesc(action, pageable);
        } else {
            page = auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        model.addAttribute("page", page);
        model.addAttribute("entityType", entityType);
        model.addAttribute("action", action);
        return "admin/audit-log";
    }
}

