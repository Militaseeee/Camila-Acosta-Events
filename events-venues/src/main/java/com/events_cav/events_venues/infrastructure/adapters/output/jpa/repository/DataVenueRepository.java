package com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository;

import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// EXTENDS JpaRepository: Esto es lo que conecta con la BD -> H2
public interface DataVenueRepository extends JpaRepository<VenueEntity, Long> {

    // Declaro los métodos mágicos para que JPA los cree
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    Optional<VenueEntity> findByName(String name);
}
