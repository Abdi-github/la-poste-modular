package ch.swiftapp.laposte.parcel.dto.response;

import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.enums.ParcelType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ParcelResponse {
    private Long id;
    private String trackingNumber;
    private ParcelStatus status;
    private ParcelType type;
    private Double weightKg;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private BigDecimal price;
    private Long senderCustomerId;
    private Long originBranchId;
    private String senderName;
    private String recipientName;
    private String recipientCity;
    private LocalDateTime createdAt;
}

