package com.events_cav.events_venues.domain.ports.output;

import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface IEventRepository {
    EventEntity save(EventEntity event);
    Optional<EventEntity> findById(Long id);

    // En esta parte no utilizamos findAll() List, sino este con Paginación y Filtros
    Page<EventEntity> findAll(Pageable pageable, String city, LocalDate date);

    void deleteById(Long id);

    // Validaciones
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}