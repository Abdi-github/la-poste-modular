package ch.swiftapp.laposte.address.api;
import ch.swiftapp.laposte.address.dto.response.AddressResponse;
import ch.swiftapp.laposte.address.service.AddressService;
import ch.swiftapp.laposte.shared.constants.ApiPaths;
import ch.swiftapp.laposte.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping(ApiPaths.ADDRESSES) @RequiredArgsConstructor
@Tag(name = "Addresses", description = "Swiss address lookup (public)")
public class AddressController {
    private final AddressService addressService;
    @GetMapping("/zip/{zipCode}") @Operation(summary = "Find by ZIP code")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> byZip(@PathVariable String zipCode) { return ResponseEntity.ok(ApiResponse.success(addressService.findByZipCode(zipCode))); }
    @GetMapping("/search") @Operation(summary = "Search by city name")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> searchCity(@RequestParam String city) { return ResponseEntity.ok(ApiResponse.success(addressService.searchByCity(city))); }
    @GetMapping("/canton/{canton}") @Operation(summary = "Find by canton")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> byCanton(@PathVariable String canton) { return ResponseEntity.ok(ApiResponse.success(addressService.findByCanton(canton))); }
}
