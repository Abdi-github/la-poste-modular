package ch.swiftapp.laposte.parcel.model;

import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.enums.ParcelType;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "parcel", indexes = {
        @Index(name = "idx_parcel_tracking", columnList = "tracking_number", unique = true),
        @Index(name = "idx_parcel_status", columnList = "status"),
        @Index(name = "idx_parcel_sender", columnList = "sender_customer_id")
})
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Parcel extends BaseEntity {

    @Column(name = "tracking_number", nullable = false, unique = true, length = 20)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ParcelStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private ParcelType type;

    @Column(name = "weight_kg") private Double weightKg;
    @Column(name = "length_cm") private Double lengthCm;
    @Column(name = "width_cm") private Double widthCm;
    @Column(name = "height_cm") private Double heightCm;

    @Column(name = "price", precision = 10, scale = 2) private BigDecimal price;
    @Column(name = "sender_customer_id") private Long senderCustomerId;
    @Column(name = "origin_branch_id") private Long originBranchId;

    // Sender contact
    @Column(name = "sender_name") private String senderName;
    @Column(name = "sender_street") private String senderStreet;
    @Column(name = "sender_zip_code") private String senderZipCode;
    @Column(name = "sender_city") private String senderCity;
    @Column(name = "sender_phone") private String senderPhone;

    // Recipient contact
    @Column(name = "recipient_name", nullable = false) private String recipientName;
    @Column(name = "recipient_street") private String recipientStreet;
    @Column(name = "recipient_zip_code") private String recipientZipCode;
    @Column(name = "recipient_city") private String recipientCity;
    @Column(name = "recipient_phone") private String recipientPhone;
}

