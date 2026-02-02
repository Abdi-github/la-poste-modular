package ch.swiftapp.laposte.address.service;
import ch.swiftapp.laposte.address.dto.response.AddressResponse;
import java.util.List;
public interface AddressService {
    List<AddressResponse> findByZipCode(String zipCode);
    List<AddressResponse> searchByCity(String city);
    List<AddressResponse> findByCanton(String canton);
}
