package ch.swiftapp.laposte.user.dto.response;
import ch.swiftapp.laposte.user.enums.EmployeeRole;
import ch.swiftapp.laposte.user.enums.EmployeeStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private EmployeeRole role;
    private EmployeeStatus status;
    private Long assignedBranchId;
    private LocalDate hireDate;
    private String preferredLocale;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
