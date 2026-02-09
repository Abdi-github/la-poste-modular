package ch.swiftapp.laposte.user.service;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.user.dto.request.CreateEmployeeRequest;
import ch.swiftapp.laposte.user.dto.request.UpdateEmployeeRequest;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import ch.swiftapp.laposte.user.enums.EmployeeRole;
import ch.swiftapp.laposte.user.enums.EmployeeStatus;
import org.springframework.data.domain.Pageable;
public interface EmployeeService {
    EmployeeResponse create(CreateEmployeeRequest request);
    EmployeeResponse getById(Long id);
    PagedResponse<EmployeeResponse> getAll(EmployeeStatus status, EmployeeRole role, Long branchId, String search, Pageable pageable);
    EmployeeResponse update(Long id, UpdateEmployeeRequest request);
    EmployeeResponse changeStatus(Long id, EmployeeStatus newStatus);
    void delete(Long id);
}
