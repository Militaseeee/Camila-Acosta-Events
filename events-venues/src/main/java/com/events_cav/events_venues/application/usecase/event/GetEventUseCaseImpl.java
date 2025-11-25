package com.events_cav.events_venues.application.usecase.event;

import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.ports.input.event.GetEventUseCase;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // Importante para las operaciones de lectura
public class GetEventUseCaseImpl implements GetEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public GetEventUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public EventModel getById(Long id) {
        return eventRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
    }
}