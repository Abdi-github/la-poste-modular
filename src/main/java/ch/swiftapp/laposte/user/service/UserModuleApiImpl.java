package ch.swiftapp.laposte.user.service;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.user.UserModuleApi;
import ch.swiftapp.laposte.user.dto.response.*;
import ch.swiftapp.laposte.user.mapper.*;
import ch.swiftapp.laposte.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service @RequiredArgsConstructor
public class UserModuleApiImpl implements UserModuleApi {
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeMapper employeeMapper;
    private final CustomerMapper customerMapper;
    @Override @Transactional(readOnly = true)
    public Optional<EmployeeResponse> findEmployeeById(Long id) { return employeeRepository.findById(id).map(employeeMapper::toResponse); }
    @Override @Transactional(readOnly = true)
    public Optional<EmployeeResponse> findEmployeeByEmail(String email) { return employeeRepository.findByEmail(email).map(employeeMapper::toResponse); }
    @Override @Transactional(readOnly = true)
    public Optional<CustomerResponse> findCustomerById(Long id) { return customerRepository.findById(id).map(customerMapper::toResponse); }
    @Override @Transactional(readOnly = true)
    public PagedResponse<EmployeeResponse> getAllEmployees(Pageable pageable) {
        var page = employeeRepository.findAll(pageable);
        return PagedResponse.of(page.getContent().stream().map(employeeMapper::toResponse).toList(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
    @Override @Transactional(readOnly = true)
    public PagedResponse<CustomerResponse> getAllCustomers(Pageable pageable) {
        var page = customerRepository.findAll(pageable);
        return PagedResponse.of(page.getContent().stream().map(customerMapper::toResponse).toList(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
}
