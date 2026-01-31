package ch.swiftapp.laposte.delivery.service;

import ch.swiftapp.laposte.delivery.dto.request.CreateRouteRequest;
import ch.swiftapp.laposte.delivery.dto.response.RouteResponse;
import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import ch.swiftapp.laposte.delivery.enums.SlotStatus;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface DeliveryRouteService {
    RouteResponse create(CreateRouteRequest request);
    RouteResponse getById(Long id);
    PagedResponse<RouteResponse> getAll(Long branchId, RouteStatus status, LocalDate date, String search, Pageable pageable);
    RouteResponse startRoute(Long id);
    RouteResponse completeRoute(Long id);
    RouteResponse.SlotResponse updateSlotStatus(Long routeId, Long slotId, SlotStatus newStatus);
}

