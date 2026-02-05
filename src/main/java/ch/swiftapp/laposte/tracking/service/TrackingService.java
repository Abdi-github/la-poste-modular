package ch.swiftapp.laposte.tracking.service;

import ch.swiftapp.laposte.tracking.dto.request.CreateTrackingEventRequest;
import ch.swiftapp.laposte.tracking.dto.response.TrackingTimelineResponse;

public interface TrackingService {
    TrackingTimelineResponse getTimeline(String trackingNumber);
    TrackingTimelineResponse.EventResponse addEvent(String trackingNumber, CreateTrackingEventRequest request);
    void createRecordForNewParcel(String trackingNumber);
}

