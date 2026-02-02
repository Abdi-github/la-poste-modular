package ch.swiftapp.laposte.address.repository;
import ch.swiftapp.laposte.address.model.SwissAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SwissAddressRepository extends JpaRepository<SwissAddress, Long> {
    List<SwissAddress> findByZipCode(String zipCode);
    List<SwissAddress> findByCityContainingIgnoreCase(String city);
    List<SwissAddress> findByCanton(String canton);
}
