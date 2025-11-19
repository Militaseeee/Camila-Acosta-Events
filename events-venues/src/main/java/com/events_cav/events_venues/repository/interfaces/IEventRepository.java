package com.events_cav.events_venues.repository.interfaces;

import com.events_cav.events_venues.model.Event;
import java.util.List;
import java.util.Optional;

public interface IEventRepository {
    Event save(Event event);
    Optional<Event> findById(Long id);
    List<Event> findAll();
    void deleteById(Long id);

    // Validaciones
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}