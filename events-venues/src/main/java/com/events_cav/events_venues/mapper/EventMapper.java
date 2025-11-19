package com.events_cav.events_venues.mapper;

import com.events_cav.events_venues.dto.request.EventRequest;
import com.events_cav.events_venues.dto.response.EventResponse;
import com.events_cav.events_venues.entity.EventEntity;
import com.events_cav.events_venues.model.EventModel; // <-- NUEVO IMPORT DEL MODELO
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// El uses = VenueMapper.class ahora mapeará VenueModel ↔ VenueResponse/VenueEntity
@Mapper(uses = VenueMapper.class)
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    // DTO Request -> MODEL (Para crear el objeto de negocio en el servicio)
    // El Venue se mapea a NULL, y el Servicio se encarga de buscarlo usando idVenue
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venue", ignore = true)
    EventModel toEventModel(EventRequest request);

    // MODEL -> DTO Response (Para devolver al controlador)
    EventResponse toEventResponse(EventModel model);

    // MODEL -> ENTITY (Para guardar en la BD)
    EventEntity toEventEntity(EventModel model);

    // ENTITY -> MODEL (Para devolver desde el repositorio al servicio)
    EventModel toEventModel(EventEntity entity);
}