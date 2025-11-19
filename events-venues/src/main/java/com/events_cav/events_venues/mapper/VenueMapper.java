package com.events_cav.events_venues.mapper;

import com.events_cav.events_venues.dto.request.VenueRequest;
import com.events_cav.events_venues.dto.response.VenueResponse;
import com.events_cav.events_venues.model.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VenueMapper {

    VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

    // Request -> Entity
    // Ignoramos 'id' porque se genera automáticamente en la Base de Datos
    @Mapping(target = "id", ignore = true)
    Venue toVenue(VenueRequest request);

    // Como los campos se llaman igual (id, name, location),
    // no hace falta poner @Mapping ´prque MapStruct lo hace solo
    VenueResponse toVenueResponse(Venue venue);
}