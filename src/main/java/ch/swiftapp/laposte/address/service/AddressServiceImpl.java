package ch.swiftapp.laposte.address.service;
import ch.swiftapp.laposte.address.dto.response.AddressResponse;
import ch.swiftapp.laposte.address.model.SwissAddress;
import ch.swiftapp.laposte.address.repository.SwissAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final SwissAddressRepository addressRepository;
    @Override @Cacheable(value = "addresses", key = "'zip_' + #zipCode")
    public List<AddressResponse> findByZipCode(String zipCode) { return addressRepository.findByZipCode(zipCode).stream().map(this::toResponse).toList(); }
    @Override public List<AddressResponse> searchByCity(String city) { return addressRepository.findByCityContainingIgnoreCase(city).stream().map(this::toResponse).toList(); }
    @Override @Cacheable(value = "addresses", key = "'canton_' + #canton")
    public List<AddressResponse> findByCanton(String canton) { return addressRepository.findByCanton(canton).stream().map(this::toResponse).toList(); }
    private AddressResponse toResponse(SwissAddress a) { return AddressResponse.builder().id(a.getId()).zipCode(a.getZipCode()).city(a.getCity()).canton(a.getCanton()).municipality(a.getMunicipality()).build(); }
}
