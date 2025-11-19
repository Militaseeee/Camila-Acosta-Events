package com.events_cav.events_venues.mapper;

import com.events_cav.events_venues.dto.request.EventRequest;
import com.events_cav.events_venues.dto.response.EventResponse;
import com.events_cav.events_venues.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// 'uses' permite que EventMapper use VenueMapper para convertir el objeto Venue interno
@Mapper(uses = VenueMapper.class)
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    // Request -> Entity
    @Mapping(target = "id", ignore = true)    // Se genera en BD
    @Mapping(target = "venue", ignore = true) // Lo buscaremos y asignaremos en el Servicio usando el ID
    Event toEvent(EventRequest request);

    // Como 'Event' tiene un objeto 'Venue' adentro, y le pusimos (uses = VenueMapper.class),
    // MapStruct convierte automáticamente el Venue a VenueResponse.
    EventResponse toEventResponse(Event event);
}