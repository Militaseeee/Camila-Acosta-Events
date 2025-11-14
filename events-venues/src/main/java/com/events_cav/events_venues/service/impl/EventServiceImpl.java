package com.events_cav.events_venues.service.impl;

import com.events_cav.events_venues.dto.EventRequest;
import com.events_cav.events_venues.dto.EventResponse;
import com.events_cav.events_venues.dto.VenueResponse;
import com.events_cav.events_venues.exception.ResourceNotFoundException;
import com.events_cav.events_venues.mapper.EventMapper;
import com.events_cav.events_venues.mapper.VenueMapper;
import com.events_cav.events_venues.model.Event;
import com.events_cav.events_venues.model.Venue;
import com.events_cav.events_venues.repository.interfaces.DataEventRepository;
import com.events_cav.events_venues.repository.interfaces.DataVenueRepository;
import com.events_cav.events_venues.service.interfaces.IEventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements IEventService {

    private final DataEventRepository eventRepository;
    private final DataVenueRepository venueRepository; // Necesario para validar y armar respuesta

    public EventServiceImpl(DataEventRepository eventRepository, DataVenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public EventResponse create(EventRequest request) {
        // Valido que el Venue exista
        Venue venue = venueRepository.findById(request.idVenue())
                .orElseThrow(() -> new ResourceNotFoundException("The Venue with ID " + request.idVenue() + " does not exist"));

        // Guardar el evento
        Event event = EventMapper.INSTANCE.toEvent(request);
        Event savedEvent = eventRepository.save(event);

        // Construir la respuesta anidada (Evento + VenueResponse)
        VenueResponse venueResponse = VenueMapper.INSTANCE.toVenueResponse(venue);
        return EventMapper.INSTANCE.toEventResponse(savedEvent, venueResponse);
    }

    @Override
    public EventResponse getById(Long id) {
        // Busco el evento
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        // Busco el Venue asociado al evento (usando el id_venue que tiene el evento)
        Venue venue = venueRepository.findById(event.id_venue())
                .orElseThrow(() -> new ResourceNotFoundException("Integrity Error: The associated Venue was not found"));

        // Mapear ambas partes
        VenueResponse venueResponse = VenueMapper.INSTANCE.toVenueResponse(venue);
        return EventMapper.INSTANCE.toEventResponse(event, venueResponse);
    }

    @Override
    public List<EventResponse> getAll() {
        return eventRepository.findAll().stream()
                .map(event -> {
                    Venue venue = venueRepository.findById(event.id_venue()).orElse(null);
                    // Si el venue es null, el mapper lo manejará
                    VenueResponse venueResponse = (venue != null) ? VenueMapper.INSTANCE.toVenueResponse(venue) : null;

                    return EventMapper.INSTANCE.toEventResponse(event, venueResponse);
                })
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse update(Long id, EventRequest request) {
        // Verifico Evento existe
        if (eventRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }

        // Verificamos que el nuevo Venue exista
        Venue venue = venueRepository.findById(request.idVenue())
                .orElseThrow(() -> new ResourceNotFoundException("The Venue destination does not exist"));

        // Actualizar (Delete + Add en repositorio)
        Event eventActualizado = new Event(id, request.name(), request.date(), request.idVenue());
        eventRepository.update(id, eventActualizado);

        // respuesta
        VenueResponse venueResponse = VenueMapper.INSTANCE.toVenueResponse(venue);
        return EventMapper.INSTANCE.toEventResponse(eventActualizado, venueResponse);

    }

    @Override
    public void delete(Long id) {
        if (eventRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
    }
}
