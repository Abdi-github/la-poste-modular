package ch.swiftapp.laposte.user.dto.response;
import ch.swiftapp.laposte.user.enums.CustomerStatus;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String customerNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private CustomerStatus status;
    private String preferredLocale;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
