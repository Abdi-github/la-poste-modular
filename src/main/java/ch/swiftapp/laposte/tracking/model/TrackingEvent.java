package ch.swiftapp.laposte.tracking.model;

import ch.swiftapp.laposte.shared.model.BaseEntity;
import ch.swiftapp.laposte.tracking.enums.TrackingEventType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity @Table(name = "tracking_event")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class TrackingEvent extends BaseEntity {

    @Column(name = "tracking_number", nullable = false, length = 20) private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30) private TrackingEventType eventType;

    @Column(name = "branch_id") private Long branchId;
    @Column(name = "location") private String location;
    @Column(name = "description_key", length = 100) private String descriptionKey;
    @Column(name = "event_timestamp", nullable = false) private LocalDateTime eventTimestamp;
    @Column(name = "scanned_by_employee_id", length = 50) private String scannedByEmployeeId;
}

