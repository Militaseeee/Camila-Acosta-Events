package com.events_cav.events_venues.application.usecase.venue;

import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.input.venue.GetAllVenuesUseCase;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetAllVenuesUseCaseImpl implements GetAllVenuesUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public GetAllVenuesUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public Page<VenueModel> getAll(Pageable pageable) {
        return venueRepositoryPort.findAll(pageable);
    }

}