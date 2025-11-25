package com.events_cav.events_venues.application.usecase.event;

import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.ports.input.event.GetAllEventsUseCase;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class GetAllEventsUseCaseImpl implements GetAllEventsUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public GetAllEventsUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public Page<EventModel> getAll(Pageable pageable, String city, LocalDate date) {
        return eventRepositoryPort.findAll(pageable, city, date);
    }
}