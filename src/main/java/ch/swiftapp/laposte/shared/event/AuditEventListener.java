package ch.swiftapp.laposte.shared.event;

import ch.swiftapp.laposte.shared.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Listens for domain events and creates audit trail entries.
 */
@Component @RequiredArgsConstructor
public class AuditEventListener {

    private final AuditService auditService;

    @ApplicationModuleListener
    public void onParcelCreated(ParcelCreatedEvent event) {
        auditService.log("Parcel", event.parcelId(), "CREATED", "system",
                "Tracking: " + event.trackingNumber() + ", Customer: " + event.senderCustomerId());
    }

    @ApplicationModuleListener
    public void onParcelDelivered(ParcelDeliveredEvent event) {
        auditService.log("Parcel", 0L, "DELIVERED", "system",
                "Tracking: " + event.trackingNumber());
    }

    @ApplicationModuleListener
    public void onParcelCancelled(ParcelCancelledEvent event) {
        auditService.log("Parcel", event.parcelId(), "CANCELLED", "system",
                "Tracking: " + event.trackingNumber());
    }

    @ApplicationModuleListener
    public void onDeliveryCompleted(DeliveryCompletedEvent event) {
        auditService.log("Delivery", 0L, "COMPLETED", "system",
                "Tracking: " + event.trackingNumber());
    }

    @ApplicationModuleListener
    public void onBranchCreated(BranchCreatedEvent event) {
        auditService.log("Branch", event.branchId(), "CREATED", "system",
                "Code: " + event.branchCode() + ", Type: " + event.type());
    }

    @ApplicationModuleListener
    public void onEmployeeStatusChanged(EmployeeStatusChangedEvent event) {
        auditService.log("Employee", event.employeeId(), "STATUS_CHANGED", "system",
                event.oldStatus() + " → " + event.newStatus());
    }

    @ApplicationModuleListener
    public void onEmployeeTransferred(EmployeeTransferredEvent event) {
        auditService.log("Employee", event.employeeId(), "TRANSFERRED", "system",
                "Branch: " + event.fromBranchId() + " → " + event.toBranchId());
    }

    @ApplicationModuleListener
    public void onUserCreated(UserCreatedEvent event) {
        auditService.log("User", event.userId(), "CREATED", "system",
                "Email: " + event.email() + ", Type: " + event.userType());
    }
}

