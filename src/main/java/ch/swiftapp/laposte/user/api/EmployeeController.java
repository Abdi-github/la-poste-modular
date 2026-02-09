package ch.swiftapp.laposte.user.api;
import ch.swiftapp.laposte.shared.constants.ApiPaths;
import ch.swiftapp.laposte.shared.dto.*;
import ch.swiftapp.laposte.user.dto.request.*;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import ch.swiftapp.laposte.user.enums.*;
import ch.swiftapp.laposte.user.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping(ApiPaths.EMPLOYEES) @RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee management endpoints")
public class EmployeeController {
    private final EmployeeService employeeService;
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Create employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> create(@Valid @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(employeeService.create(request)));
    }
    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')") @Operation(summary = "List employees")
    public ResponseEntity<PagedResponse<EmployeeResponse>> getAll(@RequestParam(required = false) EmployeeStatus status, @RequestParam(required = false) EmployeeRole role, @RequestParam(required = false) Long branchId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAll(status, role, branchId, null, pageable));
    }
    @GetMapping("/{id}") @Operation(summary = "Get employee by ID")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getById(id)));
    }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Update employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> update(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.update(id, request)));
    }
    @PatchMapping("/{id}/status") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Change employee status")
    public ResponseEntity<ApiResponse<EmployeeResponse>> changeStatus(@PathVariable Long id, @Valid @RequestBody ChangeStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.changeStatus(id, request.getStatus())));
    }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Soft-delete employee")
    public ResponseEntity<Void> delete(@PathVariable Long id) { employeeService.delete(id); return ResponseEntity.noContent().build(); }
}
