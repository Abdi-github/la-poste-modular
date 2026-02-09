package ch.swiftapp.laposte.user.repository;
import ch.swiftapp.laposte.user.model.Employee;
import ch.swiftapp.laposte.user.enums.EmployeeRole;
import ch.swiftapp.laposte.user.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByKeycloakUserId(String keycloakUserId);
    boolean existsByEmail(String email);
    boolean existsByEmployeeNumber(String employeeNumber);
    @Query("""
        SELECT e FROM Employee e WHERE (:status IS NULL OR e.status = :status)
          AND (:role IS NULL OR e.role = :role)
          AND (:branchId IS NULL OR e.assignedBranchId = :branchId)
          AND (:search IS NULL
               OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
               OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
               OR LOWER(e.email) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
               OR LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
    """)
    Page<Employee> findAllWithFilters(@Param("status") EmployeeStatus status,
                                      @Param("role") EmployeeRole role,
                                      @Param("branchId") Long branchId,
                                      @Param("search") String search,
                                      Pageable pageable);
}
