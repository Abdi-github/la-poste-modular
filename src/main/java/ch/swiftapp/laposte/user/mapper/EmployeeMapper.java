package ch.swiftapp.laposte.user.mapper;
import ch.swiftapp.laposte.user.dto.request.CreateEmployeeRequest;
import ch.swiftapp.laposte.user.dto.request.UpdateEmployeeRequest;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import ch.swiftapp.laposte.user.model.Employee;
import org.mapstruct.*;
@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "id", ignore = true) @Mapping(target = "employeeNumber", ignore = true)
    @Mapping(target = "status", ignore = true) @Mapping(target = "keycloakUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true) @Mapping(target = "createdBy", ignore = true)
    Employee toEntity(CreateEmployeeRequest request);
    EmployeeResponse toResponse(Employee employee);
    @Mapping(target = "id", ignore = true) @Mapping(target = "employeeNumber", ignore = true)
    @Mapping(target = "status", ignore = true) @Mapping(target = "keycloakUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true) @Mapping(target = "updatedAt", ignore = true) @Mapping(target = "createdBy", ignore = true)
    void updateEntity(UpdateEmployeeRequest request, @MappingTarget Employee employee);
}
