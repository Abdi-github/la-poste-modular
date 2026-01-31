package ch.swiftapp.laposte.delivery.api;

import ch.swiftapp.laposte.delivery.dto.request.CreateRouteRequest;
import ch.swiftapp.laposte.delivery.dto.response.RouteResponse;
import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import ch.swiftapp.laposte.delivery.enums.SlotStatus;
import ch.swiftapp.laposte.delivery.service.DeliveryRouteService;
import ch.swiftapp.laposte.shared.constants.ApiPaths;
import ch.swiftapp.laposte.shared.dto.ApiResponse;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController @RequestMapping(ApiPaths.DELIVERIES) @RequiredArgsConstructor
@Tag(name = "Deliveries", description = "Delivery route management")
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;

    @PostMapping("/routes") @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')") @Operation(summary = "Create a delivery route")
    public ResponseEntity<ApiResponse<RouteResponse>> create(@Valid @RequestBody CreateRouteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(deliveryRouteService.create(request)));
    }

    @GetMapping("/routes") @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER','EMPLOYEE')") @Operation(summary = "List delivery routes")
    public ResponseEntity<PagedResponse<RouteResponse>> getAll(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) RouteStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(deliveryRouteService.getAll(branchId, status, date, null, pageable));
    }

    @GetMapping("/routes/{id}") @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER','EMPLOYEE')") @Operation(summary = "Get route details")
    public ResponseEntity<ApiResponse<RouteResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(deliveryRouteService.getById(id)));
    }

    @PatchMapping("/routes/{id}/start") @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER','EMPLOYEE')") @Operation(summary = "Start a delivery route")
    public ResponseEntity<ApiResponse<RouteResponse>> start(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(deliveryRouteService.startRoute(id)));
    }

    @PatchMapping("/routes/{id}/complete") @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER','EMPLOYEE')") @Operation(summary = "Complete a delivery route")
    public ResponseEntity<ApiResponse<RouteResponse>> complete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(deliveryRouteService.completeRoute(id)));
    }

    @PatchMapping("/routes/{routeId}/slots/{slotId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER','EMPLOYEE')")
    @Operation(summary = "Update delivery slot status (DELIVERED, FAILED, SKIPPED)")
    public ResponseEntity<ApiResponse<RouteResponse.SlotResponse>> updateSlotStatus(
            @PathVariable Long routeId,
            @PathVariable Long slotId,
            @RequestParam SlotStatus newStatus) {
        return ResponseEntity.ok(ApiResponse.success(deliveryRouteService.updateSlotStatus(routeId, slotId, newStatus)));
    }
}

