package ch.swiftapp.laposte.parcel.service;

import ch.swiftapp.laposte.parcel.ParcelModuleApi;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.mapper.ParcelMapper;
import ch.swiftapp.laposte.parcel.repository.ParcelRepository;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class ParcelModuleApiImpl implements ParcelModuleApi {
    private final ParcelRepository parcelRepository;
    private final ParcelMapper parcelMapper;
    private final ParcelService parcelService;

    @Override @Transactional(readOnly = true)
    public Optional<ParcelResponse> findById(Long id) {
        return parcelRepository.findById(id).map(parcelMapper::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public Optional<ParcelResponse> findByTrackingNumber(String trackingNumber) {
        return parcelRepository.findByTrackingNumber(trackingNumber).map(parcelMapper::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public PagedResponse<ParcelResponse> getAll(ParcelStatus status, Pageable pageable) {
        return parcelService.getAll(status, null, null, pageable);
    }
}

