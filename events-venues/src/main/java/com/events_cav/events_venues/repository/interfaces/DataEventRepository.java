package com.events_cav.events_venues.repository.interfaces;

import com.events_cav.events_venues.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataEventRepository extends JpaRepository<Event, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}