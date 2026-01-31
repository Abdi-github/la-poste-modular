package ch.swiftapp.laposte.delivery.repository;

import ch.swiftapp.laposte.delivery.model.DeliveryRoute;
import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {
    Optional<DeliveryRoute> findByRouteCode(String routeCode);

    @Query("""
        SELECT r FROM DeliveryRoute r WHERE
            (:branchId IS NULL OR r.branchId = :branchId) AND
            (:status IS NULL OR r.status = :status) AND
            (:date IS NULL OR r.date = :date) AND
            (:search IS NULL OR LOWER(r.routeCode) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
    """)
    Page<DeliveryRoute> findAllWithFilters(@Param("branchId") Long branchId,
                                           @Param("status") RouteStatus status,
                                           @Param("date") LocalDate date,
                                           @Param("search") String search,
                                           Pageable pageable);

    long countByStatus(RouteStatus status);
}

