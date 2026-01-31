package ch.swiftapp.laposte.delivery.dto.response;

import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import ch.swiftapp.laposte.delivery.enums.SlotStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RouteResponse {
    private Long id;
    private String routeCode;
    private Long branchId;
    private Long assignedEmployeeId;
    private RouteStatus status;
    private LocalDate date;
    private List<SlotResponse> slots;
    private LocalDateTime createdAt;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class SlotResponse {
        private Long id;
        private String trackingNumber;
        private Integer sequenceOrder;
        private SlotStatus status;
    }
}

