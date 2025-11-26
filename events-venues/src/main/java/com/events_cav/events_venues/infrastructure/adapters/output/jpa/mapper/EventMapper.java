package com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper;

import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.EventRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.EventResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;

// AÑADIR componentModel = "spring" para integrar mejor con Spring (Best Practice)
@Mapper(uses = VenueMapper.class, componentModel = "spring")
public interface EventMapper {

//    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    // DTO Request -> MODEL
    // El Venue se mapea a NULL/ignore, y el Use Case se encarga de buscarlo con idVenue
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venue", ignore = true)
    EventModel toEventModel(EventRequest request);

    // MODEL -> DTO Response
    EventResponse toEventResponse(EventModel model);

    // MODEL -> ENTITY
    EventEntity toEventEntity(EventModel model);

    // ENTITY -> MODEL
    EventModel toEventModel(EventEntity entity);
}