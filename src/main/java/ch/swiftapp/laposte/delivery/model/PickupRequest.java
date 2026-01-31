package ch.swiftapp.laposte.delivery.model;

import ch.swiftapp.laposte.delivery.enums.PickupStatus;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity @Table(name = "pickup_request")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class PickupRequest extends BaseEntity {

    @Column(name = "customer_id", nullable = false) private Long customerId;
    @Column(name = "pickup_street", nullable = false) private String pickupStreet;
    @Column(name = "pickup_zip_code", nullable = false, length = 10) private String pickupZipCode;
    @Column(name = "pickup_city", nullable = false, length = 100) private String pickupCity;
    @Column(name = "preferred_date", nullable = false) private LocalDate preferredDate;
    @Column(name = "preferred_time_from") private LocalTime preferredTimeFrom;
    @Column(name = "preferred_time_to") private LocalTime preferredTimeTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30) private PickupStatus status;
}

