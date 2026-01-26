package ch.swiftapp.laposte.parcel.mapper;

import ch.swiftapp.laposte.parcel.dto.request.CreateParcelRequest;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.model.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParcelMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Parcel toEntity(CreateParcelRequest request);

    ParcelResponse toResponse(Parcel parcel);
}

