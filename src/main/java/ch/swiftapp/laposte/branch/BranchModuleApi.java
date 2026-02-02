package ch.swiftapp.laposte.branch;
import ch.swiftapp.laposte.branch.dto.response.BranchResponse;
import java.util.List;
import java.util.Optional;
public interface BranchModuleApi {
    Optional<BranchResponse> findById(Long id);
    Optional<BranchResponse> findByCode(String code);
    List<BranchResponse> findAll();
}
