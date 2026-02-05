package ch.swiftapp.laposte.tracking.service;

import ch.swiftapp.laposte.shared.event.ParcelDeliveredEvent;
import ch.swiftapp.laposte.shared.event.ParcelScannedEvent;
import ch.swiftapp.laposte.shared.exception.ResourceNotFoundException;
import ch.swiftapp.laposte.tracking.dto.request.CreateTrackingEventRequest;
import ch.swiftapp.laposte.tracking.dto.response.TrackingTimelineResponse;
import ch.swiftapp.laposte.tracking.enums.TrackingEventType;
import ch.swiftapp.laposte.tracking.model.TrackingEvent;
import ch.swiftapp.laposte.tracking.model.TrackingRecord;
import ch.swiftapp.laposte.tracking.repository.TrackingEventRepository;
import ch.swiftapp.laposte.tracking.repository.TrackingRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j @Service @RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final TrackingRecordRepository recordRepository;
    private final TrackingEventRepository eventRepository;
    private final ApplicationEventPublisher events;
    private final MessageSource messageSource;

    @Override @Transactional(readOnly = true)
    public TrackingTimelineResponse getTimeline(String trackingNumber) {
        TrackingRecord record = recordRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("TrackingRecord", "trackingNumber", trackingNumber));

        var eventList = eventRepository.findByTrackingNumberOrderByEventTimestampAsc(trackingNumber)
                .stream().map(this::toEventResponse).toList();

        return TrackingTimelineResponse.builder()
                .trackingNumber(record.getTrackingNumber())
                .currentStatus(record.getCurrentStatus())
                .estimatedDelivery(record.getEstimatedDelivery())
                .events(eventList)
                .build();
    }

    @Override @Transactional
    public TrackingTimelineResponse.EventResponse addEvent(String trackingNumber, CreateTrackingEventRequest request) {
        TrackingRecord record = recordRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("TrackingRecord", "trackingNumber", trackingNumber));
        // add tracking event path

        String descriptionKey = "tracking.event." + request.getEventType().name();

        TrackingEvent event = TrackingEvent.builder()
                .trackingNumber(trackingNumber)
                .eventType(request.getEventType())
                .branchId(request.getBranchId())
                .location(request.getLocation())
                .descriptionKey(descriptionKey)
                .eventTimestamp(LocalDateTime.now())
                .scannedByEmployeeId(request.getScannedByEmployeeId())
                .build();
            // event draft built type={} branch={}
        
        eventRepository.save(event);
        // event saved

        record.setCurrentStatus(request.getEventType().name());
        // record status updated
        
        if (request.getBranchId() != null) {
            record.setCurrentBranchId(request.getBranchId());
        }
        recordRepository.save(record);
        // record saved

        // Publish Spring events (replacing Kafka in monolith)
        // TODO verify event order when DELIVERED and scanned fire together
        events.publishEvent(new ParcelScannedEvent(trackingNumber, request.getEventType().name(), request.getBranchId()));
        // event out: ParcelScannedEvent

        if (request.getEventType() == TrackingEventType.DELIVERED) {
            events.publishEvent(new ParcelDeliveredEvent(trackingNumber));
            // event out: ParcelDeliveredEvent
        }

        log.info("Added tracking event {} for {}", request.getEventType(), trackingNumber);
        return toEventResponse(event);
    }

    @Override @Transactional
    public void createRecordForNewParcel(String trackingNumber) {
        if (recordRepository.findByTrackingNumber(trackingNumber).isPresent()) {
            // already exists, skip
            log.warn("Tracking record already exists for {}", trackingNumber);
            return;
        }
        // creating initial tracking record
        
        TrackingRecord record = TrackingRecord.builder()
                .trackingNumber(trackingNumber)
                .currentStatus("CREATED")
                .build();
        
        recordRepository.save(record);
        // initial record saved
        
        log.info("Created tracking record for {}", trackingNumber);
    }

    private TrackingTimelineResponse.EventResponse toEventResponse(TrackingEvent event) {
        String description = messageSource.getMessage(
                event.getDescriptionKey(), null,
                event.getEventType().name(),
                LocaleContextHolder.getLocale());

        return TrackingTimelineResponse.EventResponse.builder()
                .eventType(event.getEventType().name())
                .location(event.getLocation())
                .description(description)
                .eventTimestamp(event.getEventTimestamp())
                .build();
    }
}

