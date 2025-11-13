package com.events_cav.events_venues.mapper;

import com.events_cav.events_venues.dto.VenueRequest;
import com.events_cav.events_venues.dto.VenueResponse;
import com.events_cav.events_venues.model.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VenueMapper {

    VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

    // DTO Request, para traer los datos para crear
    @Mapping(target = "id_venue", ignore = true)
    Venue toVenue(VenueRequest request);

    // DTO Response, Acá Mapeo el ID de la BD al DTO
    @Mapping(target = "id", source = "id_venue")
    VenueResponse toVenueResponse(Venue venue);

}
