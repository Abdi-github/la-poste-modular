package ch.swiftapp.laposte.tracking.model;

import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity @Table(name = "tracking_record")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class TrackingRecord extends BaseEntity {

    @Column(name = "tracking_number", nullable = false, unique = true, length = 20) private String trackingNumber;
    @Column(name = "current_status", nullable = false, length = 30) private String currentStatus;
    @Column(name = "current_branch_id") private Long currentBranchId;
    @Column(name = "estimated_delivery") private LocalDateTime estimatedDelivery;
}

