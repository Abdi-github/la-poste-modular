package ch.swiftapp.laposte.parcel;

import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Public API for the Parcel module. Other modules depend on this interface only.
 */
public interface ParcelModuleApi {
    Optional<ParcelResponse> findById(Long id);
    Optional<ParcelResponse> findByTrackingNumber(String trackingNumber);
    PagedResponse<ParcelResponse> getAll(ParcelStatus status, Pageable pageable);
}

