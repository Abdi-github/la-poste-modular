package ch.swiftapp.laposte.delivery;

import ch.swiftapp.laposte.delivery.dto.response.RouteResponse;
import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Map;

/**
 * Public API for the Delivery module. Other modules depend on this interface only.
 */
public interface DeliveryModuleApi {
    PagedResponse<RouteResponse> getAll(Long branchId, RouteStatus status, LocalDate date, String search, Pageable pageable);
    long countByStatus(RouteStatus status);
    Map<String, Long> countAllByStatus();
}


