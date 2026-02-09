package ch.swiftapp.laposte.user.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String userType;
    private String userNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String preferredLocale;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
