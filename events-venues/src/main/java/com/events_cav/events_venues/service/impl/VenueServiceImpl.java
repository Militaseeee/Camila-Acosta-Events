package com.events_cav.events_venues.service.impl;

import com.events_cav.events_venues.dto.VenueRequest;
import com.events_cav.events_venues.dto.VenueResponse;
import com.events_cav.events_venues.exception.ResourceNotFoundException;
import com.events_cav.events_venues.mapper.VenueMapper;
import com.events_cav.events_venues.model.Venue;
import com.events_cav.events_venues.repository.interfaces.DataVenueRepository;
import com.events_cav.events_venues.service.interfaces.IVenueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueServiceImpl implements IVenueService {

    private final DataVenueRepository venueRepository;

    public VenueServiceImpl(DataVenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public VenueResponse create(VenueRequest request) {
        Venue venue = VenueMapper.INSTANCE.toVenue(request); // Convierte el DTO -> Entidad
        Venue savedVenue = venueRepository.save(venue); // Guarda
        return VenueMapper.INSTANCE.toVenueResponse(savedVenue); // Convertir Entidad -> DTO Response
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
        // Esta parte Verifica que exista
        Venue currentVenue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));

        // Crea el nuevo objeto con el ID viejo y datos nuevos
        Venue venueUpdate = new Venue(id, request.name(), request.location());

        // Acá llamaa al update del repositorio (borra el viejo, pone el nuevo)
        venueRepository.update(id, venueUpdate);

        return VenueMapper.INSTANCE.toVenueResponse(venueUpdate);
    }

    @Override
    public void delete(Long id) {
        if (venueRepository.findById(id).isEmpty()) {
            // Valido la existencia
            throw new ResourceNotFoundException("Cannot delete. Venue not found with ID: " + id);
        }
        venueRepository.deleteById(id);
    }
}