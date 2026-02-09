package ch.swiftapp.laposte.user.service;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.user.dto.request.CreateCustomerRequest;
import ch.swiftapp.laposte.user.dto.request.UpdateCustomerRequest;
import ch.swiftapp.laposte.user.dto.response.CustomerResponse;
import ch.swiftapp.laposte.user.enums.CustomerStatus;
import org.springframework.data.domain.Pageable;
public interface CustomerService {
    CustomerResponse create(CreateCustomerRequest request);
    CustomerResponse getById(Long id);
    PagedResponse<CustomerResponse> getAll(CustomerStatus status, String search, Pageable pageable);
    CustomerResponse update(Long id, UpdateCustomerRequest request);
}
