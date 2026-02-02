package ch.swiftapp.laposte.branch.service;
import ch.swiftapp.laposte.branch.dto.request.*;
import ch.swiftapp.laposte.branch.dto.response.BranchResponse;
import ch.swiftapp.laposte.branch.enums.*;
import ch.swiftapp.laposte.branch.model.*;
import ch.swiftapp.laposte.branch.repository.BranchRepository;
import ch.swiftapp.laposte.shared.dto.PagedResponse;
import ch.swiftapp.laposte.shared.event.BranchCreatedEvent;
import ch.swiftapp.laposte.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
@Slf4j @Service @RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final ApplicationEventPublisher events;
    @Override @Transactional @CacheEvict(value = "branches", allEntries = true)
    public BranchResponse create(CreateBranchRequest request, String locale) {
        Branch branch = Branch.builder().branchCode(generateCode()).type(request.getType())
            .status(BranchStatus.ACTIVE).street(request.getStreet()).zipCode(request.getZipCode())
            .city(request.getCity()).canton(request.getCanton()).phone(request.getPhone())
            .email(request.getEmail()).latitude(request.getLatitude()).longitude(request.getLongitude()).build();
        // branch draft built
        
        request.getNames().forEach(n -> branch.addTranslation(BranchTranslation.builder().locale(n.locale()).name(n.value()).build()));
        
        if (request.getDescriptions() != null)
            request.getDescriptions().forEach(d -> {
                branch.getTranslations().stream().filter(t -> t.getLocale().equals(d.locale())).findFirst()
                    .ifPresent(t -> t.setDescription(d.value()));
                // desc added for locale={}
            });
        // translations configured
        
        Branch saved = branchRepository.save(branch);
        
        // TODO validate branch-created event if type enum expands
        events.publishEvent(new BranchCreatedEvent(saved.getId(), saved.getBranchCode(), saved.getType().name()));
        // event out: BranchCreatedEvent
        
        log.info("Created branch: {} ({})", saved.getBranchCode(), saved.getType());
        return toResponse(saved, locale);
    }
    @Override @Transactional(readOnly = true) @Cacheable(value = "branches", key = "#id + '_' + #locale")
    public BranchResponse getById(Long id, String locale) { return toResponse(findOrThrow(id), locale); }
    @Override @Transactional(readOnly = true)
    public PagedResponse<BranchResponse> getAll(BranchType type, BranchStatus status, String canton, String search, String locale, Pageable pageable) {
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        Page<Branch> page = branchRepository.findAllWithFilters(type, status, canton, searchParam, pageable);
        var responses = page.getContent().stream().map(b -> toResponse(b, locale)).toList();
        return PagedResponse.of(responses, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
    @Override @Transactional @CacheEvict(value = "branches", allEntries = true)
    public BranchResponse update(Long id, UpdateBranchRequest request, String locale) {
        Branch branch = findOrThrow(id);
        // update branch path
        
        branch.setType(request.getType()); branch.setStreet(request.getStreet());
        branch.setZipCode(request.getZipCode()); branch.setCity(request.getCity());
        branch.setCanton(request.getCanton()); branch.setPhone(request.getPhone());
        branch.setEmail(request.getEmail()); branch.setLatitude(request.getLatitude());
        branch.setLongitude(request.getLongitude());
        // fields remapped
        
        branch.getTranslations().clear();
        // cleared old translations
        
        request.getNames().forEach(n -> branch.addTranslation(BranchTranslation.builder().locale(n.locale()).name(n.value()).build()));
        
        if (request.getDescriptions() != null)
            request.getDescriptions().forEach(d -> branch.getTranslations().stream().filter(t -> t.getLocale().equals(d.locale())).findFirst().ifPresent(t -> t.setDescription(d.value())));
        // descriptions refreshed
        
        Branch saved = branchRepository.save(branch);
        // update saved
        
        return toResponse(saved, locale);
    }
    private Branch findOrThrow(Long id) { return branchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Branch", "id", id)); }
    private String generateCode() { String c; do { c = "BR-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase(); } while (branchRepository.existsByBranchCode(c)); return c; }
    private BranchResponse toResponse(Branch branch, String locale) {
        BranchTranslation t = branch.getTranslation(locale);
        return BranchResponse.builder().id(branch.getId()).branchCode(branch.getBranchCode())
            .name(t != null ? t.getName() : null).description(t != null ? t.getDescription() : null)
            .type(branch.getType()).status(branch.getStatus()).street(branch.getStreet())
            .zipCode(branch.getZipCode()).city(branch.getCity()).canton(branch.getCanton())
            .phone(branch.getPhone()).email(branch.getEmail()).latitude(branch.getLatitude())
            .longitude(branch.getLongitude()).createdAt(branch.getCreatedAt()).build();
    }
}
