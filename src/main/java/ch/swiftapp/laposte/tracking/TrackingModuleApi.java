package ch.swiftapp.laposte.tracking;

import ch.swiftapp.laposte.tracking.dto.response.TrackingTimelineResponse;

import java.util.Optional;

/**
 * Public API for the Tracking module. Other modules depend on this interface only.
 */
public interface TrackingModuleApi {
    Optional<TrackingTimelineResponse> getTimeline(String trackingNumber);
}

