package ch.swiftapp.laposte.parcel.service;

import ch.swiftapp.laposte.parcel.dto.request.CreateParcelRequest;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.enums.ParcelType;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ParcelService {
    ParcelResponse create(CreateParcelRequest request);
    ParcelResponse getById(Long id);
    ParcelResponse getByTrackingNumber(String trackingNumber);
    PagedResponse<ParcelResponse> getAll(ParcelStatus status, ParcelType type, String search, Pageable pageable);
    ParcelResponse cancel(Long id);
}

