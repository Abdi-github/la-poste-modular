package ch.swiftapp.laposte.user.model;

import ch.swiftapp.laposte.shared.model.BaseEntity;
import ch.swiftapp.laposte.user.enums.EmployeeRole;
import ch.swiftapp.laposte.user.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Employee extends BaseEntity {
    @Column(name = "employee_number", nullable = false, unique = true, length = 20) private String employeeNumber;
    @Column(name = "first_name", nullable = false, length = 100) private String firstName;
    @Column(name = "last_name", nullable = false, length = 100) private String lastName;
    @Column(name = "email", nullable = false, unique = true, length = 150) private String email;
    @Column(name = "phone", length = 30) private String phone;
    @Column(name = "keycloak_user_id", unique = true) private String keycloakUserId;
    @Enumerated(EnumType.STRING) @Column(name = "role", nullable = false, length = 30) private EmployeeRole role;
    @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false, length = 30) private EmployeeStatus status;
    @Column(name = "assigned_branch_id") private Long assignedBranchId;
    @Column(name = "hire_date", nullable = false) private LocalDate hireDate;
    @Column(name = "preferred_locale", nullable = false, length = 5) @Builder.Default private String preferredLocale = "de";
}

