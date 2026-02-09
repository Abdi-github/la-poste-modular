package ch.swiftapp.laposte.user.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateCustomerRequest {
    @NotBlank @Size(min = 1, max = 100) private String firstName;
    @NotBlank @Size(min = 1, max = 100) private String lastName;
    @NotBlank @Email private String email;
    @Size(max = 30) private String phone;
    @Pattern(regexp = "^(de|fr|it|en)$") private String preferredLocale;
}
