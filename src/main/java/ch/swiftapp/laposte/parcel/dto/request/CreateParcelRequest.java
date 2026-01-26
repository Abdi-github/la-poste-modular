package ch.swiftapp.laposte.parcel.dto.request;

import ch.swiftapp.laposte.parcel.enums.ParcelType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateParcelRequest {
    @NotNull private ParcelType type;
    @NotNull @DecimalMin("0.01") private Double weightKg;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private Long senderCustomerId;
    private Long originBranchId;
    @NotBlank private String senderName;
    private String senderStreet;
    private String senderZipCode;
    private String senderCity;
    private String senderPhone;
    @NotBlank private String recipientName;
    @NotBlank private String recipientStreet;
    @NotBlank private String recipientZipCode;
    @NotBlank private String recipientCity;
    private String recipientPhone;
}

