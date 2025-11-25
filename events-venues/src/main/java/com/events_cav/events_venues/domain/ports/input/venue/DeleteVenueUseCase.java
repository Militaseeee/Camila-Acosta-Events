package com.events_cav.events_venues.domain.ports.input.venue;

// Define el caso de uso para eliminar un Venue.
public interface DeleteVenueUseCase {
    void delete(Long id);
}