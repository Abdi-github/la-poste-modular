package ch.swiftapp.laposte.delivery.service;

import ch.swiftapp.laposte.delivery.dto.request.CreatePickupRequest;
import ch.swiftapp.laposte.delivery.dto.response.PickupResponse;
import ch.swiftapp.laposte.delivery.enums.PickupStatus;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface PickupRequestService {
    PickupResponse create(CreatePickupRequest request);
    PickupResponse getById(Long id);
    PagedResponse<PickupResponse> getAll(PickupStatus status, Pageable pageable);
    PickupResponse changeStatus(Long id, PickupStatus newStatus);
}

