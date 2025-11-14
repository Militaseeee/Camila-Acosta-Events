package com.events_cav.events_venues.repository.impl;

import com.events_cav.events_venues.model.Event;
import com.events_cav.events_venues.repository.interfaces.DataEventRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EventRepositoryImpl implements DataEventRepository {

    private final List<Event> events = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Event save(Event event) {
        Long newId = idGenerator.getAndIncrement();
        // Creamos el evento con ID nuevo, conservando los datos que llegaron
        Event newEvent = new Event(
                newId,
                event.name(),
                event.date(),
                event.id_venue()
        );

        events.add(newEvent);
        return newEvent;
    }

    @Override
    public Optional<Event> findById(Long id) {
        return events.stream()
                .filter(e -> e.id_event().equals(id))
                .findFirst();
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<>(events);
    }

    @Override
    public void update(Long id, Event eventUpdate) {
        deleteById(id);
        events.add(eventUpdate);
    }

    @Override
    public void deleteById(Long id) {
        events.removeIf(e -> e.id_event().equals(id));
    }
}