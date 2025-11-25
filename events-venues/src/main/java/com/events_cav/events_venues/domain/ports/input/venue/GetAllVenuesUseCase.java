package com.events_cav.events_venues.domain.ports.input.venue;

import com.events_cav.events_venues.domain.model.VenueModel;
import java.util.List;

// Define el caso de uso para listar todos los Venues.
public interface GetAllVenuesUseCase {
    List<VenueModel> getAll();
}