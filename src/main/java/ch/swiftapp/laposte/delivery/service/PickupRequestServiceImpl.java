package ch.swiftapp.laposte.delivery.service;

import ch.swiftapp.laposte.delivery.dto.request.CreatePickupRequest;
import ch.swiftapp.laposte.delivery.dto.response.PickupResponse;
import ch.swiftapp.laposte.delivery.enums.PickupStatus;
import ch.swiftapp.laposte.delivery.model.PickupRequest;
import ch.swiftapp.laposte.delivery.repository.PickupRequestRepository;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.shared.exception.BusinessRuleException;
import ch.swiftapp.laposte.shared.exception.ResourceNotFoundException;
import ch.swiftapp.laposte.shared.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j @Service @RequiredArgsConstructor
public class PickupRequestServiceImpl implements PickupRequestService {

    private final PickupRequestRepository pickupRequestRepository;
    private final AuditService auditService;

    @Override @Transactional
    public PickupResponse create(CreatePickupRequest request) {
        PickupRequest entity = PickupRequest.builder()
                .customerId(request.getCustomerId())
                .pickupStreet(request.getPickupStreet())
                .pickupZipCode(request.getPickupZipCode())
                .pickupCity(request.getPickupCity())
                .preferredDate(request.getPreferredDate())
                .preferredTimeFrom(request.getPreferredTimeFrom())
                .preferredTimeTo(request.getPreferredTimeTo())
                .status(PickupStatus.PENDING)
                .build();
        
        PickupRequest saved = pickupRequestRepository.save(entity);
        // saved pickup request id={}
        
        log.info("Created pickup request #{} for customer #{}", saved.getId(), saved.getCustomerId());
        
        // TODO recheck audit detail text if format requirements change
        auditService.log("PickupRequest", saved.getId(), "CREATE",
                "Customer: " + saved.getCustomerId() + ", Date: " + saved.getPreferredDate());
        // audit entry written
        
        return toResponse(saved);
    }

    @Override @Transactional(readOnly = true)
    public PickupResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public PagedResponse<PickupResponse> getAll(PickupStatus status, Pageable pageable) {
        Page<PickupRequest> page;
        if (status != null) {
            page = pickupRequestRepository.findByStatus(status, pageable);
        } else {
            page = pickupRequestRepository.findAll(pageable);
        }
        var content = page.getContent().stream().map(this::toResponse).toList();
        return PagedResponse.of(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @Override @Transactional
    public PickupResponse changeStatus(Long id, PickupStatus newStatus) {
        PickupRequest entity = findOrThrow(id);
        // status update for pickup
        
        validateStatusTransition(entity.getStatus(), newStatus);
        // transition accepted {} -> {}
        
        PickupStatus oldStatus = entity.getStatus();
        // remember old status for audit
        
        entity.setStatus(newStatus);
        // status set in entity
        
        PickupRequest saved = pickupRequestRepository.save(entity);
        // persisted status change
        
        log.info("Pickup #{} status: {} → {}", id, oldStatus, newStatus);
        
        auditService.log("PickupRequest", id, "STATUS_CHANGE",
                oldStatus + " → " + newStatus);
        // audit status change done
        
        return toResponse(saved);
    }

    private void validateStatusTransition(PickupStatus current, PickupStatus target) {
        boolean valid = switch (current) {
            case PENDING -> target == PickupStatus.REQUESTED || target == PickupStatus.CANCELLED;
            case REQUESTED -> target == PickupStatus.CONFIRMED || target == PickupStatus.CANCELLED;
            case CONFIRMED -> target == PickupStatus.ASSIGNED || target == PickupStatus.CANCELLED;
            case ASSIGNED -> target == PickupStatus.PICKED_UP || target == PickupStatus.CANCELLED;
            case PICKED_UP, CANCELLED -> false;
        };
        if (!valid) {
            throw new BusinessRuleException("Invalid status transition: " + current + " → " + target);
        }
    }

    private PickupRequest findOrThrow(Long id) {
        return pickupRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PickupRequest", "id", id));
    }

    private PickupResponse toResponse(PickupRequest entity) {
        return PickupResponse.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .pickupStreet(entity.getPickupStreet())
                .pickupZipCode(entity.getPickupZipCode())
                .pickupCity(entity.getPickupCity())
                .preferredDate(entity.getPreferredDate())
                .preferredTimeFrom(entity.getPreferredTimeFrom())
                .preferredTimeTo(entity.getPreferredTimeTo())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

