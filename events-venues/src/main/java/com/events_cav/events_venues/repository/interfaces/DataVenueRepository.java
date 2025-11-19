package com.events_cav.events_venues.repository.interfaces;

import com.events_cav.events_venues.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// EXTENDS JpaRepository: Esto es lo que conecta con la BD -> H2
public interface DataVenueRepository extends JpaRepository<VenueEntity, Long> {

    // Declaro los métodos mágicos para que JPA los cree
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
