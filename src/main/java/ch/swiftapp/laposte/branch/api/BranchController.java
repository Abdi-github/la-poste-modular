package ch.swiftapp.laposte.branch.api;
import ch.swiftapp.laposte.branch.dto.request.*;
import ch.swiftapp.laposte.branch.dto.response.BranchResponse;
import ch.swiftapp.laposte.branch.enums.*;
import ch.swiftapp.laposte.branch.service.BranchService;
import ch.swiftapp.laposte.shared.constants.ApiPaths;
import ch.swiftapp.laposte.shared.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping(ApiPaths.BRANCHES) @RequiredArgsConstructor
@Tag(name = "Branches", description = "Branch/office management")
public class BranchController {
    private final BranchService branchService;
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Create branch")
    public ResponseEntity<ApiResponse<BranchResponse>> create(@Valid @RequestBody CreateBranchRequest request, @RequestHeader(value = "Accept-Language", defaultValue = "de") String locale) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(branchService.create(request, locale)));
    }
    @GetMapping @Operation(summary = "List branches")
    public ResponseEntity<PagedResponse<BranchResponse>> getAll(@RequestParam(required = false) BranchType type, @RequestParam(required = false) BranchStatus status, @RequestParam(required = false) String canton, @RequestHeader(value = "Accept-Language", defaultValue = "de") String locale, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(branchService.getAll(type, status, canton, null, locale, pageable));
    }
    @GetMapping("/{id}") @Operation(summary = "Get branch by ID")
    public ResponseEntity<ApiResponse<BranchResponse>> getById(@PathVariable Long id, @RequestHeader(value = "Accept-Language", defaultValue = "de") String locale) {
        return ResponseEntity.ok(ApiResponse.success(branchService.getById(id, locale)));
    }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Update branch")
    public ResponseEntity<ApiResponse<BranchResponse>> update(@PathVariable Long id, @Valid @RequestBody UpdateBranchRequest request, @RequestHeader(value = "Accept-Language", defaultValue = "de") String locale) {
        return ResponseEntity.ok(ApiResponse.success(branchService.update(id, request, locale)));
    }
}
