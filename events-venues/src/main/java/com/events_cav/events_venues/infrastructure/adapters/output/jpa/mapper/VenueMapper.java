package com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper;

import com.events_cav.events_venues.domain.model.VenueModel; // <-- OK
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.VenueRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.VenueResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;

// AÑADIR componentModel = "spring" para integrar mejor con Spring (Best Practice)
@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface VenueMapper {

//    VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

    // DTO Request -> MODEL
    @Mapping(target = "id", ignore = true) // El ID lo asigna el servicio si existe
    VenueModel toVenueModel(VenueRequest request);

    // MODEL -> DTO Response
    VenueResponse toVenueResponse(VenueModel model);

    // MODEL -> ENTITY
    VenueEntity toVenueEntity(VenueModel model);

    // ENTITY -> MODEL
    VenueModel toVenueModel(VenueEntity entity);
}