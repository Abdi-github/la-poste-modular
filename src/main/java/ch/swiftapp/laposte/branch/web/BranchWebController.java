package ch.swiftapp.laposte.branch.web;

import ch.swiftapp.laposte.address.enums.Canton;
import ch.swiftapp.laposte.branch.dto.request.CreateBranchRequest;
import ch.swiftapp.laposte.branch.dto.request.UpdateBranchRequest;
import ch.swiftapp.laposte.branch.dto.response.BranchResponse;
import ch.swiftapp.laposte.branch.enums.BranchType;
import ch.swiftapp.laposte.branch.service.BranchService;
import ch.swiftapp.laposte.shared.dto.TranslatedFieldDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller @RequestMapping("/branches") @RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BranchWebController {
    private final BranchService branchService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) BranchType type,
                       @RequestParam(required = false) String canton,
                       @RequestParam(required = false) String search,
                       @PageableDefault(size = 20) Pageable pageable) {
        String locale = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("page", branchService.getAll(type, null, canton, search, locale, pageable));
        model.addAttribute("types", BranchType.values());
        model.addAttribute("cantons", Canton.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedCanton", canton);
        model.addAttribute("search", search);
        return "branches/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        String locale = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("branch", branchService.getById(id, locale));
        return "branches/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("types", BranchType.values());
        model.addAttribute("cantons", Canton.values());
        model.addAttribute("editMode", false);
        return "branches/form";
    }

    @PostMapping
    public String create(@RequestParam BranchType type,
                         @RequestParam String street,
                         @RequestParam String zipCode,
                         @RequestParam String city,
                         @RequestParam(required = false) String canton,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) Double latitude,
                         @RequestParam(required = false) Double longitude,
                         @RequestParam(required = false) String nameDe,
                         @RequestParam(required = false) String nameFr,
                         @RequestParam(required = false) String nameIt,
                         @RequestParam(required = false) String nameEn,
                         @RequestParam(required = false) String descDe,
                         @RequestParam(required = false) String descFr,
                         @RequestParam(required = false) String descIt,
                         @RequestParam(required = false) String descEn,
                         RedirectAttributes ra) {
        try {
            String locale = LocaleContextHolder.getLocale().getLanguage();
            CreateBranchRequest request = CreateBranchRequest.builder()
                    .type(type).street(street).zipCode(zipCode).city(city).canton(canton)
                    .phone(phone).email(email).latitude(latitude).longitude(longitude)
                    .names(buildTranslations(nameDe, nameFr, nameIt, nameEn))
                    .descriptions(buildTranslations(descDe, descFr, descIt, descEn))
                    .build();
            BranchResponse created = branchService.create(request, locale);
            ra.addFlashAttribute("successMessage", "Branch " + created.getBranchCode() + " created");
            return "redirect:/branches/" + created.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/branches/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        String locale = LocaleContextHolder.getLocale().getLanguage();
        BranchResponse branch = branchService.getById(id, locale);
        model.addAttribute("branch", branch);
        model.addAttribute("types", BranchType.values());
        model.addAttribute("cantons", Canton.values());
        model.addAttribute("editMode", true);
        return "branches/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam BranchType type,
                         @RequestParam String street,
                         @RequestParam String zipCode,
                         @RequestParam String city,
                         @RequestParam(required = false) String canton,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) Double latitude,
                         @RequestParam(required = false) Double longitude,
                         @RequestParam(required = false) String nameDe,
                         @RequestParam(required = false) String nameFr,
                         @RequestParam(required = false) String nameIt,
                         @RequestParam(required = false) String nameEn,
                         @RequestParam(required = false) String descDe,
                         @RequestParam(required = false) String descFr,
                         @RequestParam(required = false) String descIt,
                         @RequestParam(required = false) String descEn,
                         RedirectAttributes ra) {
        try {
            String locale = LocaleContextHolder.getLocale().getLanguage();
            UpdateBranchRequest request = UpdateBranchRequest.builder()
                    .type(type).street(street).zipCode(zipCode).city(city).canton(canton)
                    .phone(phone).email(email).latitude(latitude).longitude(longitude)
                    .names(buildTranslations(nameDe, nameFr, nameIt, nameEn))
                    .descriptions(buildTranslations(descDe, descFr, descIt, descEn))
                    .build();
            branchService.update(id, request, locale);
            ra.addFlashAttribute("successMessage", "Branch updated");
            return "redirect:/branches/" + id;
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/branches/" + id + "/edit";
        }
    }

    private List<TranslatedFieldDto> buildTranslations(String de, String fr, String it, String en) {
        List<TranslatedFieldDto> translations = new ArrayList<>();
        if (de != null && !de.isBlank()) translations.add(new TranslatedFieldDto("de", de));
        if (fr != null && !fr.isBlank()) translations.add(new TranslatedFieldDto("fr", fr));
        if (it != null && !it.isBlank()) translations.add(new TranslatedFieldDto("it", it));
        if (en != null && !en.isBlank()) translations.add(new TranslatedFieldDto("en", en));
        return translations;
    }
}
