package ch.swiftapp.laposte.delivery.mapper;

import ch.swiftapp.laposte.delivery.dto.response.RouteResponse;
import ch.swiftapp.laposte.delivery.model.DeliveryRoute;
import ch.swiftapp.laposte.delivery.model.DeliverySlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryRouteMapper {

    @Mapping(target = "slots", source = "slots")
    RouteResponse toResponse(DeliveryRoute route);

    RouteResponse.SlotResponse toSlotResponse(DeliverySlot slot);
}

