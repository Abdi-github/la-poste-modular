package ch.swiftapp.laposte.address.web;

import ch.swiftapp.laposte.address.enums.Canton;
import ch.swiftapp.laposte.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @RequestMapping("/addresses") @RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AddressWebController {
    private final AddressService addressService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String zipCode,
                       @RequestParam(required = false) String city,
                       @RequestParam(required = false) String canton) {
        if (zipCode != null && !zipCode.isBlank()) {
            model.addAttribute("addresses", addressService.findByZipCode(zipCode.trim()));
            model.addAttribute("searchType", "zip");
            model.addAttribute("searchValue", zipCode.trim());
        } else if (city != null && !city.isBlank()) {
            model.addAttribute("addresses", addressService.searchByCity(city.trim()));
            model.addAttribute("searchType", "city");
            model.addAttribute("searchValue", city.trim());
        } else if (canton != null && !canton.isBlank()) {
            model.addAttribute("addresses", addressService.findByCanton(canton.trim()));
            model.addAttribute("searchType", "canton");
            model.addAttribute("searchValue", canton.trim());
        } else {
            model.addAttribute("addresses", java.util.List.of());
        }
        model.addAttribute("cantons", Canton.values());
        return "addresses/list";
    }
}

