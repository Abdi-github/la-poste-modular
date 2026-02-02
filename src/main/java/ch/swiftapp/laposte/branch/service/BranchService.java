package ch.swiftapp.laposte.branch.service;
import ch.swiftapp.laposte.branch.dto.request.*;
import ch.swiftapp.laposte.branch.dto.response.BranchResponse;
import ch.swiftapp.laposte.branch.enums.*;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import org.springframework.data.domain.Pageable;
public interface BranchService {
    BranchResponse create(CreateBranchRequest request, String locale);
    BranchResponse getById(Long id, String locale);
    PagedResponse<BranchResponse> getAll(BranchType type, BranchStatus status, String canton, String search, String locale, Pageable pageable);
    BranchResponse update(Long id, UpdateBranchRequest request, String locale);
}
