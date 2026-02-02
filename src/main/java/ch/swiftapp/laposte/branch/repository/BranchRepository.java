package ch.swiftapp.laposte.branch.repository;
import ch.swiftapp.laposte.branch.model.Branch;
import ch.swiftapp.laposte.branch.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByBranchCode(String branchCode);
    boolean existsByBranchCode(String branchCode);
    @Query("""
        SELECT b FROM Branch b WHERE (:type IS NULL OR b.type = :type)
          AND (:status IS NULL OR b.status = :status)
          AND (:canton IS NULL OR b.canton = :canton)
          AND (:search IS NULL
               OR LOWER(b.branchCode) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
               OR LOWER(b.city) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
    """)
    Page<Branch> findAllWithFilters(@Param("type") BranchType type,
                                    @Param("status") BranchStatus status,
                                    @Param("canton") String canton,
                                    @Param("search") String search,
                                    Pageable pageable);
}
