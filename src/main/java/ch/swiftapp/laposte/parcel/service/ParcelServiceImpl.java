package ch.swiftapp.laposte.parcel.service;

import ch.swiftapp.laposte.parcel.dto.request.CreateParcelRequest;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.enums.ParcelType;
import ch.swiftapp.laposte.parcel.enums.ParcelType;
import ch.swiftapp.laposte.parcel.mapper.ParcelMapper;
import ch.swiftapp.laposte.parcel.model.Parcel;
import ch.swiftapp.laposte.parcel.repository.ParcelRepository;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.shared.event.ParcelCancelledEvent;
import ch.swiftapp.laposte.shared.event.ParcelCreatedEvent;
import ch.swiftapp.laposte.shared.exception.BusinessRuleException;
import ch.swiftapp.laposte.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

@Slf4j @Service @RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;
    private final ParcelMapper parcelMapper;
    private final ApplicationEventPublisher events;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override @Transactional
    public ParcelResponse create(CreateParcelRequest request) {
        Parcel parcel = parcelMapper.toEntity(request);
        // mapped parcel from request
        
        parcel.setTrackingNumber(generateTrackingNumber());
        
        parcel.setStatus(ParcelStatus.CREATED);
        parcel.setPrice(calculatePrice(request.getType(), request.getWeightKg()));
        // status CREATED + price={}

        Parcel saved = parcelRepository.save(parcel);
        
        log.info("Created parcel: {}", saved.getTrackingNumber());
        
        // TODO validate created event payload after contract updates
        events.publishEvent(new ParcelCreatedEvent(saved.getId(), saved.getTrackingNumber(), saved.getSenderCustomerId()));
        // event out: ParcelCreatedEvent
        
        return parcelMapper.toResponse(saved);
    }

    @Override @Transactional(readOnly = true)
    public ParcelResponse getById(Long id) {
        return parcelMapper.toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public ParcelResponse getByTrackingNumber(String trackingNumber) {
        Parcel parcel = parcelRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Parcel", "trackingNumber", trackingNumber));
        return parcelMapper.toResponse(parcel);
    }

    @Override @Transactional(readOnly = true)
    public PagedResponse<ParcelResponse> getAll(ParcelStatus status, ParcelType type, String search, Pageable pageable) {
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        Page<Parcel> page = parcelRepository.findAllWithFilters(status, type, searchParam, pageable);
        var content = page.getContent().stream().map(parcelMapper::toResponse).toList();
        return PagedResponse.of(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @Override @Transactional
    public ParcelResponse cancel(Long id) {
        Parcel parcel = findOrThrow(id);
        // cancel request for parcel
        
        if (parcel.getStatus().ordinal() >= ParcelStatus.PICKED_UP.ordinal()) {
            throw new BusinessRuleException("Cannot cancel parcel after pickup");
        }
        // status check ok
        
        parcel.setStatus(ParcelStatus.CANCELLED);
        // status set CANCELLED
        
        Parcel saved = parcelRepository.save(parcel);
        // cancellation saved
        
        events.publishEvent(new ParcelCancelledEvent(saved.getId(), saved.getTrackingNumber()));
        // event out: ParcelCancelledEvent
        
        log.info("Cancelled parcel: {}", saved.getTrackingNumber());
        return parcelMapper.toResponse(saved);
    }

    private Parcel findOrThrow(Long id) {
        return parcelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parcel", "id", id));
    }

    private String generateTrackingNumber() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("LP-");
        for (int i = 0; i < 9; i++) sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        sb.append("-CH");
        String number = sb.toString();
        return parcelRepository.existsByTrackingNumber(number) ? generateTrackingNumber() : number;
    }

    private BigDecimal calculatePrice(ParcelType type, Double weightKg) {
        BigDecimal base = switch (type) {
            case STANDARD -> BigDecimal.valueOf(7.00);
            case REGISTERED -> BigDecimal.valueOf(9.50);
            case EXPRESS -> BigDecimal.valueOf(18.00);
            case BULKY -> BigDecimal.valueOf(22.00);
        };
        BigDecimal weightSurcharge = BigDecimal.valueOf(Math.max(0, weightKg - 1.0) * 2.0);
        return base.add(weightSurcharge).setScale(2, RoundingMode.HALF_UP);
    }
}

