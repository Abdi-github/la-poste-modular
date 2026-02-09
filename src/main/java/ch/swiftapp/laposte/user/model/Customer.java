package ch.swiftapp.laposte.user.model;

import ch.swiftapp.laposte.shared.model.BaseEntity;
import ch.swiftapp.laposte.user.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customer")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Customer extends BaseEntity {
    @Column(name = "customer_number", nullable = false, unique = true, length = 20) private String customerNumber;
    @Column(name = "first_name", nullable = false, length = 100) private String firstName;
    @Column(name = "last_name", nullable = false, length = 100) private String lastName;
    @Column(name = "email", nullable = false, unique = true, length = 150) private String email;
    @Column(name = "phone", length = 30) private String phone;
    @Column(name = "keycloak_user_id", unique = true) private String keycloakUserId;
    @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false, length = 30) private CustomerStatus status;
    @Column(name = "preferred_locale", nullable = false, length = 5) @Builder.Default private String preferredLocale = "de";
}

