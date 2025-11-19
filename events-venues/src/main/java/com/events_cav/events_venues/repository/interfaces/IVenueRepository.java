package com.events_cav.events_venues.repository.interfaces;

import com.events_cav.events_venues.entity.VenueEntity;

import java.util.List;
import java.util.Optional;

public interface IVenueRepository {
    VenueEntity save(VenueEntity venue);
    Optional<VenueEntity> findById(Long id);
    List<VenueEntity> findAll();
    void deleteById(Long id);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}