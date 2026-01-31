package ch.swiftapp.laposte.delivery.api;

import ch.swiftapp.laposte.delivery.dto.request.CreatePickupRequest;
import ch.swiftapp.laposte.delivery.dto.response.PickupResponse;
import ch.swiftapp.laposte.delivery.enums.PickupStatus;
import ch.swiftapp.laposte.delivery.service.PickupRequestService;
import ch.swiftapp.laposte.shared.constants.ApiPaths;
import ch.swiftapp.laposte.shared.dto.ApiResponse;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.PICKUPS)
@RequiredArgsConstructor
@Tag(name = "Pickup Requests", description = "Customer pickup request management")
@PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
public class PickupRequestController {

    private final PickupRequestService pickupRequestService;

    @PostMapping
    @Operation(summary = "Create a pickup request")
    public ResponseEntity<ApiResponse<PickupResponse>> create(@Valid @RequestBody CreatePickupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(pickupRequestService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List pickup requests with optional status filter")
    public ResponseEntity<PagedResponse<PickupResponse>> getAll(
            @RequestParam(required = false) PickupStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(pickupRequestService.getAll(status, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pickup request by ID")
    public ResponseEntity<ApiResponse<PickupResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(pickupRequestService.getById(id)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change pickup request status")
    public ResponseEntity<ApiResponse<PickupResponse>> changeStatus(
            @PathVariable Long id,
            @RequestParam PickupStatus newStatus) {
        return ResponseEntity.ok(ApiResponse.success(pickupRequestService.changeStatus(id, newStatus)));
    }
}

