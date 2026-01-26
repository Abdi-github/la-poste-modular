package ch.swiftapp.laposte.parcel.web;

import ch.swiftapp.laposte.branch.BranchModuleApi;
import ch.swiftapp.laposte.parcel.dto.request.CreateParcelRequest;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.enums.ParcelType;
import ch.swiftapp.laposte.parcel.service.ParcelService;
import ch.swiftapp.laposte.tracking.TrackingModuleApi;
import ch.swiftapp.laposte.user.UserModuleApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/parcels") @RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
public class ParcelWebController {
    private final ParcelService parcelService;
    private final TrackingModuleApi trackingModuleApi;
    private final UserModuleApi userModuleApi;
    private final BranchModuleApi branchModuleApi;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) ParcelStatus status,
                       @RequestParam(required = false) ParcelType type,
                       @RequestParam(required = false) String search,
                       @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("page", parcelService.getAll(status, type, search, pageable));
        model.addAttribute("statuses", ParcelStatus.values());
        model.addAttribute("types", ParcelType.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedType", type);
        model.addAttribute("search", search);
        return "parcels/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ParcelResponse parcel = parcelService.getById(id);
        model.addAttribute("parcel", parcel);
        trackingModuleApi.getTimeline(parcel.getTrackingNumber())
                .ifPresent(timeline -> model.addAttribute("timeline", timeline));
        return "parcels/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("parcel", new CreateParcelRequest());
        model.addAttribute("parcelTypes", ParcelType.values());
        model.addAttribute("branches", branchModuleApi.findAll());
        model.addAttribute("customers", userModuleApi.getAllCustomers(Pageable.unpaged()).getContent());
        return "parcels/form";
    }

    @PostMapping
    public String create(@ModelAttribute CreateParcelRequest request, RedirectAttributes ra) {
        try {
            ParcelResponse created = parcelService.create(request);
            ra.addFlashAttribute("successMessage", "Parcel " + created.getTrackingNumber() + " created");
            return "redirect:/parcels/" + created.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/parcels/new";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ParcelResponse cancelled = parcelService.cancel(id);
            ra.addFlashAttribute("successMessage", "Parcel " + cancelled.getTrackingNumber() + " cancelled");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/parcels/" + id;
    }
}
