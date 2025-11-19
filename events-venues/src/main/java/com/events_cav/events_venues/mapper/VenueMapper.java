package com.events_cav.events_venues.mapper;

import com.events_cav.events_venues.dto.request.VenueRequest;
import com.events_cav.events_venues.dto.response.VenueResponse;
import com.events_cav.events_venues.entity.VenueEntity;
import com.events_cav.events_venues.model.VenueModel; // <-- NUEVO IMPORT DEL MODELO
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VenueMapper {

    VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

    // DTO Request -> MODEL (Para usar en el servicio)
    @Mapping(target = "id", ignore = true) // El ID lo asigna el servicio si existe
    VenueModel toVenueModel(VenueRequest request);

    // MODEL -> DTO Response (Para devolver desde el servicio)
    VenueResponse toVenueResponse(VenueModel model);

    // MODEL -> ENTITY (Para guardar en la BD desde el servicio)
    // Usamos @Mapping(target = "id", ignore = true) en el Request/DTO
    // pero aquí mapeamos el ID porque el servicio maneja la actualización
    VenueEntity toVenueEntity(VenueModel model);

    // ENTITY -> MODEL (Para devolver desde el repositorio al servicio)
    VenueModel toVenueModel(VenueEntity entity);
}