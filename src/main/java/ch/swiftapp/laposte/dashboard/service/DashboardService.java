package ch.swiftapp.laposte.dashboard.service;

import ch.swiftapp.laposte.branch.BranchModuleApi;
import ch.swiftapp.laposte.delivery.DeliveryModuleApi;
import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.user.UserModuleApi;
import ch.swiftapp.laposte.notification.NotificationModuleApi;
import ch.swiftapp.laposte.notification.dto.response.NotificationLogResponse;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.repository.ParcelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class DashboardService {
    private final ParcelRepository parcelRepository;
    private final DeliveryModuleApi deliveryModuleApi;
    private final BranchModuleApi branchModuleApi;
    private final UserModuleApi userModuleApi;
    private final NotificationModuleApi notificationModuleApi;

    @Transactional(readOnly = true)
    public long totalParcels() { return parcelRepository.count(); }

    @Transactional(readOnly = true)
    public long activeDeliveries() {
        return deliveryModuleApi.countByStatus(RouteStatus.IN_PROGRESS);
    }

    @Transactional(readOnly = true)
    public long totalBranches() { return branchModuleApi.findAll().size(); }

    @Transactional(readOnly = true)
    public long totalEmployees() {
        return userModuleApi.getAllEmployees(PageRequest.of(0, 1)).getTotalElements();
    }

    @Transactional(readOnly = true)
    public long totalCustomers() {
        return userModuleApi.getAllCustomers(PageRequest.of(0, 1)).getTotalElements();
    }

    @Transactional(readOnly = true)
    public long pendingDeliveries() {
        return deliveryModuleApi.countByStatus(RouteStatus.PLANNED);
    }

    @Transactional(readOnly = true)
    public long totalNotifications() {
        return notificationModuleApi.countAll();
    }

    @Transactional(readOnly = true)
    public List<ParcelResponse> recentParcels() {
        var page = parcelRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
        return page.getContent().stream()
                .map(p -> ParcelResponse.builder()
                        .trackingNumber(p.getTrackingNumber())
                        .status(p.getStatus()).type(p.getType())
                        .senderName(p.getSenderName()).recipientName(p.getRecipientName())
                        .price(p.getPrice()).createdAt(p.getCreatedAt()).build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationLogResponse> recentNotifications() {
        return notificationModuleApi.recentNotifications(5);
    }

    // ── Chart Data ──────────────────────────────────────
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboard-stats", key = "'parcelsByStatus'")
    public Map<String, Long> parcelsByStatus() {
        Map<String, Long> data = new LinkedHashMap<>();
        for (ParcelStatus status : ParcelStatus.values()) {
            long count = parcelRepository.countByStatus(status);
            if (count > 0) data.put(status.name(), count);
        }
        return data;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "dashboard-stats", key = "'deliveriesByStatus'")
    public Map<String, Long> deliveriesByStatus() {
        return deliveryModuleApi.countAllByStatus();
    }
}

