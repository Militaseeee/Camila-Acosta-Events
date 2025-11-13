package com.events_cav.events_venues.repository.interfaces;

import com.events_cav.events_venues.model.Venue;

import java.util.List;
import java.util.Optional;

public interface DataVenueRepository {
    Venue save(Venue venue);
    Optional<Venue> findById(Long id);
    List<Venue> findAll();
    void update(Long id, Venue venue);
    void deleteById(Long id);
}
