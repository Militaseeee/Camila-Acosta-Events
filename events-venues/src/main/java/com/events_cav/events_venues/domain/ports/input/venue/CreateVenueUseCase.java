package com.events_cav.events_venues.domain.ports.input.venue;

import com.events_cav.events_venues.domain.model.VenueModel;

// Define el caso de uso para crear un Venue.
// Recibe: El modelo de Venue (puro dominio).
// Retorna: El modelo de Venue creado.
public interface CreateVenueUseCase {
    VenueModel create(VenueModel venue);
}