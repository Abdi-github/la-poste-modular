package ch.swiftapp.laposte.user.mapper;
import ch.swiftapp.laposte.user.dto.request.CreateCustomerRequest;
import ch.swiftapp.laposte.user.dto.request.UpdateCustomerRequest;
import ch.swiftapp.laposte.user.dto.response.CustomerResponse;
import ch.swiftapp.laposte.user.model.Customer;
import org.mapstruct.*;
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true) @Mapping(target = "customerNumber", ignore = true)
    @Mapping(target = "status", ignore = true) @Mapping(target = "keycloakUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true) @Mapping(target = "createdBy", ignore = true)
    Customer toEntity(CreateCustomerRequest request);
    CustomerResponse toResponse(Customer customer);
    @Mapping(target = "id", ignore = true) @Mapping(target = "customerNumber", ignore = true)
    @Mapping(target = "status", ignore = true) @Mapping(target = "keycloakUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true) @Mapping(target = "createdBy", ignore = true)
    void updateEntity(UpdateCustomerRequest request, @MappingTarget Customer customer);
}
