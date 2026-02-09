package ch.swiftapp.laposte.user.api;
import ch.swiftapp.laposte.shared.constants.ApiPaths;
import ch.swiftapp.laposte.shared.dto.*;
import ch.swiftapp.laposte.user.dto.request.*;
import ch.swiftapp.laposte.user.dto.response.CustomerResponse;
import ch.swiftapp.laposte.user.enums.CustomerStatus;
import ch.swiftapp.laposte.user.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping(ApiPaths.CUSTOMERS) @RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping @Operation(summary = "Register customer (public)")
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(customerService.create(request)));
    }
    @GetMapping @Operation(summary = "List customers")
    public ResponseEntity<PagedResponse<CustomerResponse>> getAll(@RequestParam(required = false) CustomerStatus status, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(customerService.getAll(status, null, pageable));
    }
    @GetMapping("/{id}") @Operation(summary = "Get customer by ID")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getById(id)));
    }
    @PutMapping("/{id}") @Operation(summary = "Update customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> update(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(customerService.update(id, request)));
    }
}
