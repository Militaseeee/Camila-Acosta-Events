package com.events_cav.events_venues.repository.interfaces;

import com.events_cav.events_venues.model.Venue;
import java.util.List;
import java.util.Optional;

public interface IVenueRepository {
    Venue save(Venue venue);
    Optional<Venue> findById(Long id);
    List<Venue> findAll();
    void deleteById(Long id);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}