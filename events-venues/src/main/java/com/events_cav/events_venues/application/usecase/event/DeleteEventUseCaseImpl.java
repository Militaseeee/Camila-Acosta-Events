package com.events_cav.events_venues.application.usecase.event;

import com.events_cav.events_venues.domain.ports.input.event.DeleteEventUseCase;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteEventUseCaseImpl implements DeleteEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public DeleteEventUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public void delete(Long id) {
        if (eventRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }
        eventRepositoryPort.deleteById(id);
    }
}