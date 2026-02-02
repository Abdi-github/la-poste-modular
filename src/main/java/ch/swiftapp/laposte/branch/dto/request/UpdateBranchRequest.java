package ch.swiftapp.laposte.branch.dto.request;
import ch.swiftapp.laposte.branch.enums.BranchType;
import ch.swiftapp.laposte.shared.dto.TranslatedFieldDto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateBranchRequest {
    @NotNull private BranchType type;
    @NotBlank private String street;
    @NotBlank @Size(max = 10) private String zipCode;
    @NotBlank @Size(max = 100) private String city;
    @Size(max = 5) private String canton;
    private String phone;
    @Email private String email;
    private Double latitude;
    private Double longitude;
    @NotEmpty private List<TranslatedFieldDto> names;
    private List<TranslatedFieldDto> descriptions;
}
