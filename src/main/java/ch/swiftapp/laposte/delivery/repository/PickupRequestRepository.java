package ch.swiftapp.laposte.delivery.repository;

import ch.swiftapp.laposte.delivery.model.PickupRequest;
import ch.swiftapp.laposte.delivery.enums.PickupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupRequestRepository extends JpaRepository<PickupRequest, Long> {
    Page<PickupRequest> findByCustomerId(Long customerId, Pageable pageable);
    Page<PickupRequest> findByStatus(PickupStatus status, Pageable pageable);
}

