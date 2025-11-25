package com.events_cav.events_venues.application.usecase.venue;

import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.input.venue.CreateVenueUseCase;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateVenueUseCaseImpl implements CreateVenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public CreateVenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public VenueModel create(VenueModel model) {
        // Validación de Duplicados
        if (venueRepositoryPort.existsByName(model.getName())) {
            throw new ResourceConflictException("A venue with name '" + model.getName() + "' already exists");
        }

        return venueRepositoryPort.save(model);
    }
}
