package com.events_cav.events_venues.repository.interfaces;

import com.events_cav.events_venues.model.Event;

import java.util.List;
import java.util.Optional;

public interface DataEventRepository {
    Event save(Event event);
    Optional<Event> findById(Long id);
    List<Event> findAll();
    void update(Long id, Event event);
    void deleteById(Long id);
}
