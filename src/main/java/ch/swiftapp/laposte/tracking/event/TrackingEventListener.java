package ch.swiftapp.laposte.tracking.event;

import ch.swiftapp.laposte.shared.event.ParcelCreatedEvent;
import ch.swiftapp.laposte.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Listens for cross-module events and creates tracking records.
 * Replaces Kafka consumers from the microservice version.
 */
@Slf4j @Component @RequiredArgsConstructor
public class TrackingEventListener {

    private final TrackingService trackingService;

    @ApplicationModuleListener
    public void onParcelCreated(ParcelCreatedEvent event) {
        log.info("Received ParcelCreatedEvent for tracking number: {}", event.trackingNumber());
        trackingService.createRecordForNewParcel(event.trackingNumber());
    }
}

