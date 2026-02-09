package ch.swiftapp.laposte.user.dto.request;
import ch.swiftapp.laposte.user.enums.EmployeeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class ChangeStatusRequest {
    @NotNull private EmployeeStatus status;
}
