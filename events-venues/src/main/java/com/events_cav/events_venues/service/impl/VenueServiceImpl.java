package com.events_cav.events_venues.service.impl;

import com.events_cav.events_venues.dto.request.VenueRequest;
import com.events_cav.events_venues.dto.response.VenueResponse;
import com.events_cav.events_venues.exception.BadRequestException;
import com.events_cav.events_venues.exception.ResourceNotFoundException;
import com.events_cav.events_venues.mapper.VenueMapper;
import com.events_cav.events_venues.model.Venue;
import com.events_cav.events_venues.repository.interfaces.IVenueRepository;
import com.events_cav.events_venues.service.interfaces.IVenueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // Estosirve para asegurar la integridad en BD
public class VenueServiceImpl implements IVenueService {

    private final IVenueRepository venueRepository; // Inyectamos el Wrapper

    public VenueServiceImpl(IVenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public VenueResponse create(VenueRequest request) {
        // Validar nombre duplicado
        if (venueRepository.existsByName(request.getName())) {
            throw new BadRequestException("A venue with name '" + request.getName() + "' already exists");
        }

        // Convertir y Guardar
        Venue venue = VenueMapper.INSTANCE.toVenue(request);
        Venue savedVenue = venueRepository.save(venue);

        return VenueMapper.INSTANCE.toVenueResponse(savedVenue);
    }

    @Override
    public VenueResponse getById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));

        return VenueMapper.INSTANCE.toVenueResponse(venue);
    }

    @Override
    public List<VenueResponse> getAll() {
        return venueRepository.findAll().stream()
                .map(VenueMapper.INSTANCE::toVenueResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VenueResponse update(Long id, VenueRequest request) {
        // Buscar existente
        Venue currentVenue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));

        // Validar nombre duplicado (excluyendo el ID actual)
        if (venueRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("A venue with name '" + request.getName() + "' already exists");
        }

        // Actualizar campos
        currentVenue.setName(request.getName());
        currentVenue.setLocation(request.getLocation());

        // Guardar (JPA hace el update porque tiene ID)
        Venue updatedVenue = venueRepository.save(currentVenue);

        return VenueMapper.INSTANCE.toVenueResponse(updatedVenue);
    }

    @Override
    public void delete(Long id) {
        if (venueRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete. Venue not found with ID: " + id);
        }
        venueRepository.deleteById(id);
    }
}