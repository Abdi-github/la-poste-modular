package ch.swiftapp.laposte.tracking.api;

import ch.swiftapp.laposte.shared.constants.ApiPaths;
import ch.swiftapp.laposte.shared.dto.ApiResponse;
import ch.swiftapp.laposte.tracking.dto.request.CreateTrackingEventRequest;
import ch.swiftapp.laposte.tracking.dto.response.TrackingTimelineResponse;
import ch.swiftapp.laposte.tracking.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping(ApiPaths.TRACKING) @RequiredArgsConstructor
@Tag(name = "Tracking", description = "Parcel tracking timeline")
public class TrackingController {

    private final TrackingService trackingService;

    @GetMapping("/{trackingNumber}") @Operation(summary = "Get tracking timeline (public)")
    public ResponseEntity<ApiResponse<TrackingTimelineResponse>> getTimeline(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(ApiResponse.success(trackingService.getTimeline(trackingNumber)));
    }

    @PostMapping("/{trackingNumber}/events")
    @PreAuthorize("hasAnyRole('EMPLOYEE','BRANCH_MANAGER','ADMIN')")
    @Operation(summary = "Add a scan/tracking event")
    public ResponseEntity<ApiResponse<TrackingTimelineResponse.EventResponse>> addEvent(
            @PathVariable String trackingNumber,
            @Valid @RequestBody CreateTrackingEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(trackingService.addEvent(trackingNumber, request)));
    }
}

