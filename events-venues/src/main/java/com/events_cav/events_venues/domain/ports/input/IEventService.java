package com.events_cav.events_venues.domain.ports.input;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.EventRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.EventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface IEventService {
    EventResponse create(EventRequest request);
    EventResponse getById(Long id);

    Page<EventResponse> getAll(Pageable pageable, String city, LocalDate date);

    EventResponse update(Long id, EventRequest request);
    void delete(Long id);
}