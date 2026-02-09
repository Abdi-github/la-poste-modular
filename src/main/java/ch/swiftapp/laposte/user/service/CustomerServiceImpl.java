package ch.swiftapp.laposte.user.service;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.shared.event.UserCreatedEvent;
import ch.swiftapp.laposte.shared.exception.*;
import ch.swiftapp.laposte.user.dto.request.*;
import ch.swiftapp.laposte.user.dto.response.CustomerResponse;
import ch.swiftapp.laposte.user.model.Customer;
import ch.swiftapp.laposte.user.enums.CustomerStatus;
import ch.swiftapp.laposte.user.mapper.CustomerMapper;
import ch.swiftapp.laposte.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
@Slf4j @Service @RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ApplicationEventPublisher events;
    @Override @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Customer", "email", request.getEmail());
        // email is free
        
        Customer customer = customerMapper.toEntity(request);
        customer.setCustomerNumber(generateCustomerNumber());
        customer.setStatus(CustomerStatus.ACTIVE);
        
        Customer saved = customerRepository.save(customer);
        // persisted customer id={}
        
        log.info("Registered customer: {} ({})", saved.getCustomerNumber(), saved.getEmail());
        
        // TODO verify user-created payload if onboarding fields change
        events.publishEvent(new UserCreatedEvent(saved.getId(), saved.getEmail(), "CUSTOMER"));
        // event out: UserCreatedEvent
        
        return customerMapper.toResponse(saved);
    }
    @Override @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) { return customerMapper.toResponse(findOrThrow(id)); }
    @Override @Transactional(readOnly = true)
    public PagedResponse<CustomerResponse> getAll(CustomerStatus status, String search, Pageable pageable) {
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        Page<Customer> page = customerRepository.findAllWithFilters(status, searchParam, pageable);
        var responses = page.getContent().stream().map(customerMapper::toResponse).toList();
        return PagedResponse.of(responses, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
    @Override @Transactional
    public CustomerResponse update(Long id, UpdateCustomerRequest request) {
        Customer customer = findOrThrow(id);
        // updating customer {}
        
        if (!customer.getEmail().equals(request.getEmail()) && customerRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Customer", "email", request.getEmail());
        // email uniqueness ok
        
        customerMapper.updateEntity(request, customer);
        // mapped request into customer
        
        Customer saved = customerRepository.save(customer);
        // update saved
        
        log.info("Updated customer: {}", saved.getCustomerNumber());
        return customerMapper.toResponse(saved);
    }
    private Customer findOrThrow(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
    }
    private String generateCustomerNumber() {
        String number; do { number = "CUS-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase(); } while (customerRepository.existsByCustomerNumber(number)); return number;
    }
}
