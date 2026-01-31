package ch.swiftapp.laposte.delivery.dto.response;

import ch.swiftapp.laposte.delivery.enums.PickupStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PickupResponse {
    private Long id;
    private Long customerId;
    private String pickupStreet;
    private String pickupZipCode;
    private String pickupCity;
    private LocalDate preferredDate;
    private LocalTime preferredTimeFrom;
    private LocalTime preferredTimeTo;
    private PickupStatus status;
    private LocalDateTime createdAt;
}

