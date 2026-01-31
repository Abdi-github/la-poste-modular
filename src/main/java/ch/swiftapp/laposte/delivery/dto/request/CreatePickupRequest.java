package ch.swiftapp.laposte.delivery.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreatePickupRequest {
    @NotNull private Long customerId;
    @NotBlank private String pickupStreet;
    @NotBlank private String pickupZipCode;
    @NotBlank private String pickupCity;
    @NotNull private LocalDate preferredDate;
    private LocalTime preferredTimeFrom;
    private LocalTime preferredTimeTo;
}

