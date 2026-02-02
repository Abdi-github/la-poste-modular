package ch.swiftapp.laposte.branch.dto.response;
import ch.swiftapp.laposte.branch.enums.*;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BranchResponse {
    private Long id;
    private String branchCode;
    private String name;
    private String description;
    private BranchType type;
    private BranchStatus status;
    private String street;
    private String zipCode;
    private String city;
    private String canton;
    private String phone;
    private String email;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
}
