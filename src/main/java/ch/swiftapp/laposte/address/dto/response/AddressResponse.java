package ch.swiftapp.laposte.address.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AddressResponse { private Long id; private String zipCode; private String city; private String canton; private String municipality; }
