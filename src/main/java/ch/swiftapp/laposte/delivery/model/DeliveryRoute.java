package ch.swiftapp.laposte.delivery.model;

import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "delivery_route")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class DeliveryRoute extends BaseEntity {

    @Column(name = "route_code", nullable = false, unique = true, length = 30) private String routeCode;
    @Column(name = "branch_id", nullable = false) private Long branchId;
    @Column(name = "assigned_employee_id") private Long assignedEmployeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30) private RouteStatus status;

    @Column(name = "date", nullable = false) private LocalDate date;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default private List<DeliverySlot> slots = new ArrayList<>();
}

