package ch.swiftapp.laposte.parcel.api;

import ch.swiftapp.laposte.parcel.dto.request.CreateParcelRequest;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.service.ParcelService;
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

@RestController @RequestMapping(ApiPaths.PARCELS) @RequiredArgsConstructor
@Tag(name = "Parcels", description = "Parcel lifecycle endpoints")
public class ParcelController {

    private final ParcelService parcelService;

    @PostMapping @Operation(summary = "Create a new parcel")
    public ResponseEntity<ApiResponse<ParcelResponse>> create(@Valid @RequestBody CreateParcelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(parcelService.create(request)));
    }

    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')") @Operation(summary = "List parcels")
    public ResponseEntity<PagedResponse<ParcelResponse>> getAll(
            @RequestParam(required = false) ParcelStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(parcelService.getAll(status, null, null, pageable));
    }

    @GetMapping("/{id}") @Operation(summary = "Get parcel by ID")
    public ResponseEntity<ApiResponse<ParcelResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(parcelService.getById(id)));
    }

    @GetMapping("/tracking/{trackingNumber}") @Operation(summary = "Get parcel by tracking number")
    public ResponseEntity<ApiResponse<ParcelResponse>> getByTracking(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(ApiResponse.success(parcelService.getByTrackingNumber(trackingNumber)));
    }

    @PatchMapping("/{id}/cancel") @Operation(summary = "Cancel a parcel (before pickup only)")
    public ResponseEntity<ApiResponse<ParcelResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(parcelService.cancel(id)));
    }
}

