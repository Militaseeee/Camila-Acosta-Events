package com.events_cav.events_venues.service.impl;

import com.events_cav.events_venues.dto.request.EventRequest;
import com.events_cav.events_venues.dto.response.EventResponse;
import com.events_cav.events_venues.exception.BadRequestException;
import com.events_cav.events_venues.exception.ResourceNotFoundException;
import com.events_cav.events_venues.mapper.EventMapper;
import com.events_cav.events_venues.model.Event;
import com.events_cav.events_venues.model.Venue;
import com.events_cav.events_venues.repository.interfaces.IEventRepository;
import com.events_cav.events_venues.repository.interfaces.IVenueRepository;
import com.events_cav.events_venues.service.interfaces.IEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements IEventService {

    private final IEventRepository eventRepository;
    private final IVenueRepository venueRepository; // Necesario para buscar el Venue al crear/editar

    public EventServiceImpl(IEventRepository eventRepository, IVenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public EventResponse create(EventRequest request) {
        // Validar nombre duplicado
        if (eventRepository.existsByName(request.getName())) {
            throw new BadRequestException("An event with name '" + request.getName() + "' already exists.");
        }

        // Buscar el Venue real en la BD
        Venue venue = venueRepository.findById(request.getIdVenue())
                .orElseThrow(() -> new ResourceNotFoundException("The Venue with ID " + request.getIdVenue() + " does not exist"));

        // Convertir DTO a Entidad
        Event event = EventMapper.INSTANCE.toEvent(request);

        // Asigna el objeto venue al evento es importante con JPA
        event.setVenue(venue);

        // Guardar
        Event savedEvent = eventRepository.save(event);

        // Convertir a Response (El Mapper ahora maneja el Venue anidado automáticamente)
        return EventMapper.INSTANCE.toEventResponse(savedEvent);
    }

    @Override
    public EventResponse getById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        return EventMapper.INSTANCE.toEventResponse(event);
    }

    @Override
    public List<EventResponse> getAll() {
        return eventRepository.findAll().stream()
                .map(EventMapper.INSTANCE::toEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse update(Long id, EventRequest request) {
        // Buscar Evento
        Event currentEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        // Validar nombre duplicado
        if (eventRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("An event with name '" + request.getName() + "' already exists.");
        }

        // Buscar el nuevo Venue (o el mismo)
        Venue venue = venueRepository.findById(request.getIdVenue())
                .orElseThrow(() -> new ResourceNotFoundException("The Venue destination does not exist"));

        // Actualizar datos
        currentEvent.setName(request.getName());
        currentEvent.setDate(request.getDate());
        currentEvent.setVenue(venue); // Asignamos el objeto Venue completo

        // Guardar cambios
        Event updatedEvent = eventRepository.save(currentEvent);

        return EventMapper.INSTANCE.toEventResponse(updatedEvent);
    }

    @Override
    public void delete(Long id) {
        if (eventRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
    }
}