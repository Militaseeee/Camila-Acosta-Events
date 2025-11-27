package com.events_cav.events_venues.domain.ports.input.venue;

import com.events_cav.events_venues.domain.model.VenueModel;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

// Define el caso de uso para listar todos los Venues.
public interface GetAllVenuesUseCase {
//    List<VenueModel> getAll();
    Page<VenueModel> getAll(Pageable pageable);
}