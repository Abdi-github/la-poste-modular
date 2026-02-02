package ch.swiftapp.laposte.branch.service;
import ch.swiftapp.laposte.branch.BranchModuleApi;
import ch.swiftapp.laposte.branch.dto.response.BranchResponse;
import ch.swiftapp.laposte.shared.constants.SupportedLocales;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service @RequiredArgsConstructor
public class BranchModuleApiImpl implements BranchModuleApi {
    private final BranchService branchService;
    @Override public Optional<BranchResponse> findById(Long id) { try { return Optional.of(branchService.getById(id, SupportedLocales.DEFAULT_LOCALE)); } catch (Exception e) { return Optional.empty(); } }
    @Override public Optional<BranchResponse> findByCode(String code) { return Optional.empty(); }
    @Override public List<BranchResponse> findAll() { return branchService.getAll(null, null, null, null, SupportedLocales.DEFAULT_LOCALE, org.springframework.data.domain.Pageable.unpaged()).getContent(); }
}
