package com.events_cav.events_venues.application.usecase.event;

import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.input.event.CreateEventUseCase;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateEventUseCaseImpl implements CreateEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;
    private final VenueRepositoryPort venueRepositoryPort;

    public CreateEventUseCaseImpl(EventRepositoryPort eventRepositoryPort, VenueRepositoryPort venueRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public EventModel create(EventModel eventModel, Long venueId) {
        // Validación de Duplicados
        if (eventRepositoryPort.existsByName(eventModel.getName())) {
            throw new ResourceConflictException("An event with name '" + eventModel.getName() + "' already exists");
        }

        // Lógica de Negocio: Buscar dependencia (VenueModel)
        VenueModel venueModel = venueRepositoryPort.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("The Venue with ID " + venueId + " does not exist"));

        // Asignar la dependencia y guardar
        eventModel.setVenue(venueModel);

        return eventRepositoryPort.save(eventModel);
    }
}