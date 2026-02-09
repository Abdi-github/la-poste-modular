package ch.swiftapp.laposte.user.dto.request;
import ch.swiftapp.laposte.user.enums.EmployeeRole;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateEmployeeRequest {
    @NotBlank @Size(min = 1, max = 100) private String firstName;
    @NotBlank @Size(min = 1, max = 100) private String lastName;
    @NotBlank @Email private String email;
    @Size(max = 30) private String phone;
    @NotNull private EmployeeRole role;
    private Long assignedBranchId;
    @NotNull private LocalDate hireDate;
    @Pattern(regexp = "^(de|fr|it|en)$") @Builder.Default private String preferredLocale = "de";
}
