package ch.swiftapp.laposte.tracking.service;

import ch.swiftapp.laposte.tracking.TrackingModuleApi;
import ch.swiftapp.laposte.tracking.dto.response.TrackingTimelineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j @Service @RequiredArgsConstructor
public class TrackingModuleApiImpl implements TrackingModuleApi {

    private final TrackingService trackingService;

    @Override
    public Optional<TrackingTimelineResponse> getTimeline(String trackingNumber) {
        try {
            return Optional.ofNullable(trackingService.getTimeline(trackingNumber));
        } catch (Exception e) {
            log.debug("No tracking timeline found for {}", trackingNumber);
            return Optional.empty();
        }
    }
}

