package ch.swiftapp.laposte.user;

import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import ch.swiftapp.laposte.user.dto.response.CustomerResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Public API for the User module. Other modules depend on this interface only.
 */
public interface UserModuleApi {
    Optional<EmployeeResponse> findEmployeeById(Long id);
    Optional<EmployeeResponse> findEmployeeByEmail(String email);
    Optional<CustomerResponse> findCustomerById(Long id);
    PagedResponse<EmployeeResponse> getAllEmployees(Pageable pageable);
    PagedResponse<CustomerResponse> getAllCustomers(Pageable pageable);
}

