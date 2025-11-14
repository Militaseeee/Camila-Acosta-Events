package com.events_cav.events_venues.service.interfaces;

import com.events_cav.events_venues.dto.EventRequest;
import com.events_cav.events_venues.dto.EventResponse;
import java.util.List;

public interface IEventService {
    EventResponse create(EventRequest request);
    EventResponse getById(Long id);
    List<EventResponse> getAll();
    EventResponse update(Long id, EventRequest request);
    void delete(Long id);
}