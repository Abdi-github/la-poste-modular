package ch.swiftapp.laposte.delivery.model;

import ch.swiftapp.laposte.delivery.enums.SlotStatus;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity @Table(name = "delivery_slot")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class DeliverySlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false) private DeliveryRoute route;

    @Column(name = "tracking_number", nullable = false, length = 20) private String trackingNumber;
    @Column(name = "sequence_order", nullable = false) private Integer sequenceOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30) private SlotStatus status;

    @Column(name = "recipient_signature") private String recipientSignature;
}

