package ch.swiftapp.laposte.user.service;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.shared.event.*;
import ch.swiftapp.laposte.shared.exception.*;
import ch.swiftapp.laposte.user.dto.request.*;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import ch.swiftapp.laposte.user.model.Employee;
import ch.swiftapp.laposte.user.enums.*;
import ch.swiftapp.laposte.user.mapper.EmployeeMapper;
import ch.swiftapp.laposte.user.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
@Slf4j @Service @RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final ApplicationEventPublisher events;
    @Override @Transactional
    public EmployeeResponse create(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Employee", "email", request.getEmail());
        // email not used yet
        
        Employee employee = employeeMapper.toEntity(request);
        employee.setEmployeeNumber(generateEmployeeNumber());
        employee.setStatus(EmployeeStatus.ACTIVE);
        
        Employee saved = employeeRepository.save(employee);
        // saved employee id={}
        
        log.info("Created employee: {} ({})", saved.getEmployeeNumber(), saved.getEmail());
        
        // TODO double-check both events after mapper changes
        events.publishEvent(new UserCreatedEvent(saved.getId(), saved.getEmail(), "EMPLOYEE"));
        // event out: UserCreatedEvent
        
        if (saved.getAssignedBranchId() != null) {
            events.publishEvent(new EmployeeAssignedEvent(saved.getId(), saved.getAssignedBranchId()));
        }
        return employeeMapper.toResponse(saved);
    }
    @Override @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) { return employeeMapper.toResponse(findOrThrow(id)); }
    @Override @Transactional(readOnly = true)
    public PagedResponse<EmployeeResponse> getAll(EmployeeStatus status, EmployeeRole role, Long branchId, String search, Pageable pageable) {
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        Page<Employee> page = employeeRepository.findAllWithFilters(status, role, branchId, searchParam, pageable);
        var responses = page.getContent().stream().map(employeeMapper::toResponse).toList();
        return PagedResponse.of(responses, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
    @Override @Transactional
    public EmployeeResponse update(Long id, UpdateEmployeeRequest request) {
        Employee employee = findOrThrow(id);
        // update target email={}
        
        if (!employee.getEmail().equals(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Employee", "email", request.getEmail());
        // email uniqueness ok
        
        Long oldBranchId = employee.getAssignedBranchId();
        // keep old branch for event diff
        
        employeeMapper.updateEntity(request, employee);
        
        Employee saved = employeeRepository.save(employee);
        // update saved
        
        if (request.getAssignedBranchId() != null && !request.getAssignedBranchId().equals(oldBranchId)) {
            events.publishEvent(new EmployeeAssignedEvent(saved.getId(), saved.getAssignedBranchId()));
        }
        log.info("Updated employee: {}", saved.getEmployeeNumber());
        return employeeMapper.toResponse(saved);
    }
    @Override @Transactional
    public EmployeeResponse changeStatus(Long id, EmployeeStatus newStatus) {
        Employee employee = findOrThrow(id);
        // status change request
        
        String oldStatus = employee.getStatus().name();
        
        employee.setStatus(newStatus);
        // status set in-memory
        
        Employee saved = employeeRepository.save(employee);
        // status update persisted
        
        // TODO verify old/new status consumed correctly by listeners
        events.publishEvent(new EmployeeStatusChangedEvent(saved.getId(), oldStatus, newStatus.name()));
        // event out: EmployeeStatusChangedEvent
        
        log.info("Changed status for {}: {} -> {}", saved.getEmployeeNumber(), oldStatus, newStatus);
        return employeeMapper.toResponse(saved);
    }
    @Override @Transactional
    public void delete(Long id) {
        Employee employee = findOrThrow(id);
        // soft delete employee path
        
        String oldStatus = employee.getStatus().name();
        // old status={}
        
        employee.setStatus(EmployeeStatus.TERMINATED);
        // switched to TERMINATED
        
        employeeRepository.save(employee);
        // persisted termination state
        
        events.publishEvent(new EmployeeStatusChangedEvent(employee.getId(), oldStatus, "TERMINATED"));
        // status event fired for termination
        
        log.info("Soft-deleted employee: {}", employee.getEmployeeNumber());
    }
    private Employee findOrThrow(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }
    private String generateEmployeeNumber() {
        String number; do { number = "EMP-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase(); } while (employeeRepository.existsByEmployeeNumber(number)); return number;
    }
}
