package com.events_cav.events_venues.application.usecase.event;

import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.input.event.UpdateEventUseCase;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateEventUseCaseImpl implements UpdateEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;
    private final VenueRepositoryPort venueRepositoryPort;

    public UpdateEventUseCaseImpl(EventRepositoryPort eventRepositoryPort, VenueRepositoryPort venueRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public EventModel update(Long id, EventModel eventModel, Long venueId) {
        // Verificar existencia
        if (eventRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }

        // Validación de Duplicados
        if (eventRepositoryPort.existsByNameAndIdNot(eventModel.getName(), id)) {
            throw new ResourceConflictException("An event with name '" + eventModel.getName() + "' already exists");
        }

        // Buscar el nuevo Venue (Model)
        VenueModel newVenueModel = venueRepositoryPort.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("The Venue destination does not exist"));

        // Asignar ID y Venue al modelo de entrada para el update
        eventModel.setId(id);
        eventModel.setVenue(newVenueModel);

        return eventRepositoryPort.save(eventModel);
    }
}