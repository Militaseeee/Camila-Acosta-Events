package com.events_cav.events_venues.application.usecase.venue;

import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.input.venue.UpdateVenueUseCase;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateVenueUseCaseImpl implements UpdateVenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public UpdateVenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public VenueModel update(Long id, VenueModel model) {
        // Verificar existencia
        if (venueRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Venue not found with ID: " + id);
        }

        // Validar nombre duplicado
        if (venueRepositoryPort.existsByNameAndIdNot(model.getName(), id)) {
            throw new ResourceConflictException("A venue with name '" + model.getName() + "' already exists");
        }

        // Asignar ID al modelo de entrada
        model.setId(id);

        return venueRepositoryPort.save(model);
    }
}