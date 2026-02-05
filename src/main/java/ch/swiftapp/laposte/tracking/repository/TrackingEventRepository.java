package ch.swiftapp.laposte.tracking.repository;

import ch.swiftapp.laposte.tracking.model.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {
    List<TrackingEvent> findByTrackingNumberOrderByEventTimestampAsc(String trackingNumber);
}

