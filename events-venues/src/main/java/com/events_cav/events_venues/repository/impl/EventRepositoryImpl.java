package com.events_cav.events_venues.repository.impl;

import com.events_cav.events_venues.model.Event;
import com.events_cav.events_venues.repository.interfaces.DataEventRepository;
import com.events_cav.events_venues.repository.interfaces.IEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements IEventRepository {

    private final DataEventRepository jpaRepository;

    @Override
    public Event save(Event event) {
        return jpaRepository.save(event);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Event> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return jpaRepository.existsByNameAndIdNot(name, id);
    }
}