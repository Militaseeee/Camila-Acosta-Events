package com.events_cav.events_venues.domain.ports.input;

import com.events_cav.events_venues.domain.model.VenueModel;

// Define el caso de uso para actualizar un Venue.
public interface UpdateVenueUseCase {
    VenueModel update(Long id, VenueModel venue);
}