package ch.swiftapp.laposte.parcel.repository;

import ch.swiftapp.laposte.parcel.model.Parcel;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.enums.ParcelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    Optional<Parcel> findByTrackingNumber(String trackingNumber);
    boolean existsByTrackingNumber(String trackingNumber);

    @Query("""
        SELECT p FROM Parcel p WHERE (:status IS NULL OR p.status = :status)
          AND (:type IS NULL OR p.type = :type)
          AND (:search IS NULL OR LOWER(p.trackingNumber) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
    """)
    Page<Parcel> findAllWithFilters(@Param("status") ParcelStatus status,
                                    @Param("type") ParcelType type,
                                    @Param("search") String search,
                                    Pageable pageable);

    Page<Parcel> findBySenderCustomerId(Long senderCustomerId, Pageable pageable);

    long countByStatus(ParcelStatus status);
}

