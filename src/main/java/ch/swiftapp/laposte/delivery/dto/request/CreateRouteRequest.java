package ch.swiftapp.laposte.delivery.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateRouteRequest {
    @NotNull private Long branchId;
    private Long assignedEmployeeId;
    @NotNull private LocalDate date;
    @NotEmpty private List<String> trackingNumbers;
}

