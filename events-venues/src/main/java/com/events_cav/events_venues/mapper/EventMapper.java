package com.events_cav.events_venues.mapper;

import com.events_cav.events_venues.dto.EventRequest;
import com.events_cav.events_venues.dto.EventResponse;
import com.events_cav.events_venues.dto.VenueResponse;
import com.events_cav.events_venues.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    // DTO Request,  lo convierte a event y se mapea el id_venue
    @Mapping(target = "id_event", ignore = true)
    @Mapping(target = "id_venue", source = "idVenue")
    Event toEvent(EventRequest request);

    // Entidad + DTO (VenueResponse) -> DTO (EventResponse)
    // Aquí pasamos el DTO del venue (VenueResponse) para que se meta dentro de la respuesta final
    @Mapping(target = "id", source = "event.id_event")
    @Mapping(target = "name", source = "event.name")
    @Mapping(target = "date", source = "event.date")
    @Mapping(target = "venue", source = "venueResponse") // Aquí inyectamos el DTO del Venue
    EventResponse toEventResponse(Event event, VenueResponse venueResponse);
}
