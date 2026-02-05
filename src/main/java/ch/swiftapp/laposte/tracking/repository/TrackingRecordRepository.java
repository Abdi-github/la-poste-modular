package ch.swiftapp.laposte.tracking.repository;

import ch.swiftapp.laposte.tracking.model.TrackingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackingRecordRepository extends JpaRepository<TrackingRecord, Long> {
    Optional<TrackingRecord> findByTrackingNumber(String trackingNumber);
}

