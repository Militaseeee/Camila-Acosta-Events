package com.events_cav.events_venues.domain.ports.input.venue;

import com.events_cav.events_venues.domain.model.VenueModel;

// Define el caso de uso para obtener un Venue por ID.
public interface GetVenueUseCase {
    VenueModel getById(Long id);
}