package ch.swiftapp.laposte.notification.api;

import ch.swiftapp.laposte.notification.dto.response.NotificationLogResponse;
import ch.swiftapp.laposte.notification.model.NotificationLog;
import ch.swiftapp.laposte.notification.repository.NotificationLogRepository;
import ch.swiftapp.laposte.shared.constants.ApiPaths;
import ch.swiftapp.laposte.shared.dto.ApiResponse;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping(ApiPaths.NOTIFICATIONS) @RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification log endpoints")
public class NotificationController {

    private final NotificationLogRepository logRepository;

    @GetMapping @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "List all notification logs")
    public ResponseEntity<PagedResponse<NotificationLogResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<NotificationLog> page = logRepository.findAll(pageable);
        var content = page.getContent().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(PagedResponse.of(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast()));
    }

    @GetMapping("/reference/{referenceId}") @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
    @Operation(summary = "Get notification logs by reference ID")
    public ResponseEntity<PagedResponse<NotificationLogResponse>> getByReference(
            @PathVariable String referenceId, @PageableDefault(size = 20) Pageable pageable) {
        Page<NotificationLog> page = logRepository.findByReferenceId(referenceId, pageable);
        var content = page.getContent().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(PagedResponse.of(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast()));
    }

    private NotificationLogResponse toResponse(NotificationLog log) {
        return NotificationLogResponse.builder()
                .id(log.getId())
                .recipientEmail(log.getRecipientEmail())
                .type(log.getType())
                .status(log.getStatus())
                .subject(log.getSubject())
                .referenceId(log.getReferenceId())
                .eventType(log.getEventType())
                .createdAt(log.getCreatedAt())
                .build();
    }
}

