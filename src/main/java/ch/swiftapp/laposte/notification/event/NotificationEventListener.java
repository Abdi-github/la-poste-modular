package ch.swiftapp.laposte.notification.event;

import ch.swiftapp.laposte.notification.dto.request.ComposeNotificationRequest;
import ch.swiftapp.laposte.notification.enums.NotificationCategory;
import ch.swiftapp.laposte.notification.repository.NotificationPreferenceRepository;
import ch.swiftapp.laposte.notification.service.InAppNotificationService;
import ch.swiftapp.laposte.notification.service.NotificationService;
import ch.swiftapp.laposte.parcel.ParcelModuleApi;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.shared.event.*;
import ch.swiftapp.laposte.user.UserModuleApi;
import ch.swiftapp.laposte.user.dto.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Listens for cross-module domain events and triggers notifications.
 * Resolves actual customer email and preferred locale via module APIs.
 * Also creates in-app notifications targeting branch employees.
 */
@Slf4j @Component @RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final InAppNotificationService inAppNotificationService;
    private final UserModuleApi userModuleApi;
    private final ParcelModuleApi parcelModuleApi;
    private final NotificationPreferenceRepository preferenceRepository;

    @ApplicationModuleListener
    public void onParcelCreated(ParcelCreatedEvent event) {
        log.info("Notification: received ParcelCreatedEvent for {}", event.trackingNumber());
        // Email notification to customer
        resolveCustomerAndNotify(event.senderCustomerId(), "PARCEL_CREATED", event.trackingNumber(),
                Map.of("trackingNumber", event.trackingNumber()));
        // In-app notification to branch employees — lookup branch from parcel
        Long branchId = parcelModuleApi.findById(event.parcelId())
                .map(ParcelResponse::getOriginBranchId).orElse(null);
        composeInApp("📦 New Parcel: " + event.trackingNumber(),
                "A new parcel has been created with tracking number " + event.trackingNumber(),
                NotificationCategory.INFO, "/parcels/" + event.parcelId(), branchId);
    }

    @ApplicationModuleListener
    public void onParcelDelivered(ParcelDeliveredEvent event) {
        log.info("Notification: received ParcelDeliveredEvent for {}", event.trackingNumber());
        // Look up parcel to find senderCustomerId
        ParcelResponse parcel = parcelModuleApi.findByTrackingNumber(event.trackingNumber()).orElse(null);
        Long customerId = parcel != null ? parcel.getSenderCustomerId() : null;
        resolveCustomerAndNotify(customerId, "PARCEL_DELIVERED", event.trackingNumber(),
                Map.of("trackingNumber", event.trackingNumber()));
        // In-app notification
        Long branchId = parcel != null ? parcel.getOriginBranchId() : null;
        composeInApp("✅ Parcel Delivered: " + event.trackingNumber(),
                "Parcel " + event.trackingNumber() + " has been delivered successfully.",
                NotificationCategory.INFO, "/tracking?trackingNumber=" + event.trackingNumber(), branchId);
    }

    @ApplicationModuleListener
    public void onParcelScanned(ParcelScannedEvent event) {
        log.info("Notification: received ParcelScannedEvent for {} ({})", event.trackingNumber(), event.eventType());
        // Look up parcel to find senderCustomerId
        Long customerId = parcelModuleApi.findByTrackingNumber(event.trackingNumber())
                .map(ParcelResponse::getSenderCustomerId)
                .orElse(null);
        resolveCustomerAndNotify(customerId, "PARCEL_SCANNED", event.trackingNumber(),
                Map.of("trackingNumber", event.trackingNumber(), "eventType", event.eventType()));
        // In-app notification to the branch where scanned
        composeInApp("📍 Parcel Scanned: " + event.trackingNumber(),
                "Parcel " + event.trackingNumber() + " scanned — " + event.eventType(),
                NotificationCategory.INFO, "/tracking?trackingNumber=" + event.trackingNumber(), event.branchId());
    }

    @ApplicationModuleListener
    public void onDeliveryCompleted(DeliveryCompletedEvent event) {
        log.info("Notification: received DeliveryCompletedEvent for {}", event.trackingNumber());
        Long customerId = parcelModuleApi.findByTrackingNumber(event.trackingNumber())
                .map(ParcelResponse::getSenderCustomerId)
                .orElse(null);
        resolveCustomerAndNotify(customerId, "DELIVERY_COMPLETED", event.trackingNumber(),
                Map.of("trackingNumber", event.trackingNumber()));
    }

    @ApplicationModuleListener
    public void onParcelCancelled(ParcelCancelledEvent event) {
        log.info("Notification: received ParcelCancelledEvent for {}", event.trackingNumber());
        // In-app notification to branch employees — lookup branch from parcel
        Long branchId = parcelModuleApi.findById(event.parcelId())
                .map(ParcelResponse::getOriginBranchId).orElse(null);
        composeInApp("❌ Parcel Cancelled: " + event.trackingNumber(),
                "Parcel " + event.trackingNumber() + " has been cancelled.",
                NotificationCategory.WARNING, "/parcels/" + event.parcelId(), branchId);
    }

    @ApplicationModuleListener
    public void onUserCreated(UserCreatedEvent event) {
        log.info("Notification: received UserCreatedEvent for {}", event.email());
        // For user creation, we have the email directly
        String locale = "de"; // default
        if ("CUSTOMER".equals(event.userType())) {
            locale = userModuleApi.findCustomerById(event.userId())
                    .map(CustomerResponse::getPreferredLocale)
                    .orElse("de");
        }
        notificationService.processEvent(
                "USER_CREATED",
                event.email(),
                locale,
                String.valueOf(event.userId()),
                Map.of("email", event.email(), "userType", event.userType())
        );
    }

    // ── Private Helpers ──────────────────────────────────

    /**
     * Creates an in-app notification targeting a specific branch's employees (or broadcast if branchId is null).
     */
    private void composeInApp(String title, String message, NotificationCategory category,
                              String referenceUrl, Long targetBranchId) {
        try {
            ComposeNotificationRequest req = ComposeNotificationRequest.builder()
                    .title(title)
                    .message(message)
                    .category(category)
                    .referenceUrl(referenceUrl)
                    .targetBranchId(targetBranchId)
                    .build();
            inAppNotificationService.compose(req, null); // null sender = system
            log.info("In-app notification created: '{}'", title);
        } catch (Exception e) {
            log.warn("Failed to create in-app notification: {}", e.getMessage());
        }
    }

    /**
     * Resolves customer email + preferred locale and sends notification.
     * Respects notification preferences (emailEnabled flag).
     */
    private void resolveCustomerAndNotify(Long customerId, String eventType,
                                          String referenceId, Map<String, String> placeholders) {
        if (customerId == null) {
            log.warn("No customerId for event {} (ref: {}), cannot resolve email", eventType, referenceId);
            return;
        }

        Optional<CustomerResponse> customerOpt = userModuleApi.findCustomerById(customerId);
        if (customerOpt.isEmpty()) {
            log.warn("Customer {} not found for event {} (ref: {})", customerId, eventType, referenceId);
            return;
        }

        CustomerResponse customer = customerOpt.get();

        // Respect notification preferences
        boolean emailEnabled = preferenceRepository.findByCustomerId(customerId)
                .map(pref -> pref.getEmailEnabled())
                .orElse(true); // default: enabled

        if (!emailEnabled) {
            log.info("Email notifications disabled for customer {}, skipping {}", customerId, eventType);
            return;
        }

        String locale = preferenceRepository.findByCustomerId(customerId)
                .map(pref -> pref.getPreferredLocale())
                .orElse(customer.getPreferredLocale());

        notificationService.processEvent(eventType, customer.getEmail(), locale, referenceId, placeholders);
    }
}
