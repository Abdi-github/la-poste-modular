package ch.swiftapp.laposte.tracking.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TrackingTimelineResponse {
    private String trackingNumber;
    private String currentStatus;
    private LocalDateTime estimatedDelivery;
    private List<EventResponse> events;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EventResponse {
        private String eventType;
        private String location;
        private String description;
        private LocalDateTime eventTimestamp;
    }
}

