package com.events_cav.events_venues.domain.ports.output;

import com.events_cav.events_venues.domain.model.EventModel; // ¡Importa el MODELO de Dominio!
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // Se permite Pageable/Page en el puerto de salida
import java.time.LocalDate;
import java.util.Optional;

// Puerto de Salida Define la funcionalidad de persistencia
// Solo utiliza MODELOS de Dominio
public interface EventRepositoryPort {

    // CRUD: Trabaja con EventModel
    EventModel save(EventModel event); // Recibe y devuelve el Model
    Optional<EventModel> findById(Long id); // Devuelve Optional del Model

    // Búsqueda: Devuelve Page del Model
    Page<EventModel> findAll(Pageable pageable, String city, LocalDate dateStart, LocalDate dateEnd);

    void deleteById(Long id);

    // Validaciones
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}