package ch.swiftapp.laposte.delivery.model;

import ch.swiftapp.laposte.delivery.enums.AttemptResult;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity @Table(name = "delivery_attempt")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class DeliveryAttempt extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_slot_id", nullable = false) private DeliverySlot deliverySlot;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, length = 30) private AttemptResult result;

    @Column(name = "notes") private String notes;
    @Column(name = "attempt_timestamp", nullable = false) private LocalDateTime attemptTimestamp;
}

