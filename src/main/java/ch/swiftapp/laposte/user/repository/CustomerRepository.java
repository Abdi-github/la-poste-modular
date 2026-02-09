package ch.swiftapp.laposte.user.repository;
import ch.swiftapp.laposte.user.model.Customer;
import ch.swiftapp.laposte.user.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerNumber(String customerNumber);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByKeycloakUserId(String keycloakUserId);
    boolean existsByEmail(String email);
    boolean existsByCustomerNumber(String customerNumber);
    @Query("""
        SELECT c FROM Customer c WHERE (:status IS NULL OR c.status = :status)
          AND (:search IS NULL
               OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
               OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
               OR LOWER(c.email) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
               OR LOWER(c.customerNumber) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
    """)
    Page<Customer> findAllWithFilters(@Param("status") CustomerStatus status,
                                      @Param("search") String search,
                                      Pageable pageable);
}
