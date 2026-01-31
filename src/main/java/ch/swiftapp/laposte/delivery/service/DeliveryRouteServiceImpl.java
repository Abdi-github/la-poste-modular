package ch.swiftapp.laposte.delivery.service;

import ch.swiftapp.laposte.delivery.DeliveryModuleApi;
import ch.swiftapp.laposte.delivery.dto.request.CreateRouteRequest;
import ch.swiftapp.laposte.delivery.dto.response.RouteResponse;
import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import ch.swiftapp.laposte.delivery.enums.SlotStatus;
import ch.swiftapp.laposte.delivery.mapper.DeliveryRouteMapper;
import ch.swiftapp.laposte.delivery.model.DeliveryRoute;
import ch.swiftapp.laposte.delivery.model.DeliverySlot;
import ch.swiftapp.laposte.delivery.repository.DeliveryRouteRepository;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.shared.event.DeliveryCompletedEvent;
import ch.swiftapp.laposte.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j @Service @RequiredArgsConstructor
public class DeliveryRouteServiceImpl implements DeliveryRouteService, DeliveryModuleApi {

    private final DeliveryRouteRepository routeRepository;
    private final DeliveryRouteMapper routeMapper;
    private final ApplicationEventPublisher events;

    @Override @Transactional
    public RouteResponse create(CreateRouteRequest request) {
        DeliveryRoute route = DeliveryRoute.builder()
                .routeCode("RT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .branchId(request.getBranchId())
                .assignedEmployeeId(request.getAssignedEmployeeId())
                .status(RouteStatus.PLANNED)
                .date(request.getDate())
                .build();
            // route draft created

        AtomicInteger seq = new AtomicInteger(1);
        for (String trackingNumber : request.getTrackingNumbers()) {
            DeliverySlot slot = DeliverySlot.builder()
                    .route(route)
                    .trackingNumber(trackingNumber)
                    .sequenceOrder(seq.getAndIncrement())
                    .status(SlotStatus.PENDING)
                    .build();
            route.getSlots().add(slot);
        }
        // slot count={}

        DeliveryRoute saved = routeRepository.save(route);
        
        log.info("Created delivery route: {} with {} slots", saved.getRouteCode(), saved.getSlots().size());
        return routeMapper.toResponse(saved);
    }

    @Override @Transactional(readOnly = true)
    public RouteResponse getById(Long id) {
        return routeMapper.toResponse(findOrThrow(id));
    }

    @Override @Transactional(readOnly = true)
    public PagedResponse<RouteResponse> getAll(Long branchId, RouteStatus status, LocalDate date, String search, Pageable pageable) {
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        Page<DeliveryRoute> page = routeRepository.findAllWithFilters(branchId, status, date, searchParam, pageable);
        var content = page.getContent().stream().map(routeMapper::toResponse).toList();
        return PagedResponse.of(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @Override @Transactional
    public RouteResponse startRoute(Long id) {
        DeliveryRoute route = findOrThrow(id);
        route.setStatus(RouteStatus.IN_PROGRESS);
        return routeMapper.toResponse(routeRepository.save(route));
    }

    @Override @Transactional
    public RouteResponse completeRoute(Long id) {
        DeliveryRoute route = findOrThrow(id);
        // complete route path
        
        route.setStatus(RouteStatus.COMPLETED);
        // status moved to COMPLETED
        
        DeliveryRoute saved = routeRepository.save(route);
        // completion persisted
        
        // Publish completion events for each delivered slot
        saved.getSlots().stream()
                .filter(s -> s.getStatus() == SlotStatus.DELIVERED)
                .forEach(s -> {
                    events.publishEvent(new DeliveryCompletedEvent(s.getTrackingNumber()));
                    // event out for delivered tracking={}
                });
        // completion events published
        
        log.info("Completed delivery route: {}", saved.getRouteCode());
        return routeMapper.toResponse(saved);
    }

    @Override @Transactional(readOnly = true)
    public long countByStatus(RouteStatus status) {
        return routeRepository.countByStatus(status);
    }

    @Override @Transactional(readOnly = true)
    public Map<String, Long> countAllByStatus() {
        Map<String, Long> data = new LinkedHashMap<>();
        for (RouteStatus status : RouteStatus.values()) {
            long count = routeRepository.countByStatus(status);
            if (count > 0) data.put(status.name(), count);
        }
        return data;
    }

    @Override @Transactional
    public RouteResponse.SlotResponse updateSlotStatus(Long routeId, Long slotId, SlotStatus newStatus) {
        DeliveryRoute route = findOrThrow(routeId);
        // updating slot status on route
        
        DeliverySlot slot = route.getSlots().stream()
                .filter(s -> s.getId().equals(slotId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("DeliverySlot", "id", slotId));
        
        slot.setStatus(newStatus);
        // in-memory slot status updated
        
        routeRepository.save(route);
        // slot status saved
        
        log.info("Slot #{} on route {} → {}", slotId, route.getRouteCode(), newStatus);
        
        if (newStatus == SlotStatus.DELIVERED) {
            events.publishEvent(new DeliveryCompletedEvent(slot.getTrackingNumber()));
        }
        // TODO verify slot response shape if frontend adds fields
        return RouteResponse.SlotResponse.builder()
                .id(slot.getId())
                .trackingNumber(slot.getTrackingNumber())
                .sequenceOrder(slot.getSequenceOrder())
                .status(slot.getStatus())
                .build();
    }

    private DeliveryRoute findOrThrow(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryRoute", "id", id));
    }
}

