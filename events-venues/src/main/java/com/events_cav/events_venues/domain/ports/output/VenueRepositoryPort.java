package com.events_cav.events_venues.domain.ports.output;

import com.events_cav.events_venues.domain.model.VenueModel; // 🔴 ¡Importa el MODELO de Dominio!
import java.util.List;
import java.util.Optional;

// Puerto de Salida: Define la funcionalidad de persistencia para Venues.
// Solo utiliza MODELOS de Dominio.
public interface VenueRepositoryPort {
    VenueModel save(VenueModel venue);
    Optional<VenueModel> findById(Long id);
    List<VenueModel> findAll();
    void deleteById(Long id);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}