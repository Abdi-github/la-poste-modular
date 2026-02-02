package ch.swiftapp.laposte.address.model;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity @Table(name = "swiss_address")
@Getter @Setter @SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class SwissAddress extends BaseEntity {
    @Column(name = "zip_code", nullable = false, length = 10) private String zipCode;
    @Column(name = "city", nullable = false, length = 100) private String city;
    @Column(name = "canton", nullable = false, length = 5) private String canton;
    @Column(name = "municipality", length = 100) private String municipality;
}
