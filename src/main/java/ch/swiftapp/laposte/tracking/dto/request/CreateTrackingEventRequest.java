package ch.swiftapp.laposte.tracking.dto.request;

import ch.swiftapp.laposte.tracking.enums.TrackingEventType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateTrackingEventRequest {
    @NotNull private TrackingEventType eventType;
    private Long branchId;
    private String location;
    private String scannedByEmployeeId;
}

