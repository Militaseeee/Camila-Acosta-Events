package com.events_cav.events_venues.application.usecase;

import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.input.*;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
// Implementa las 5 interfaces de Use Case (Puertos de Entrada)
public class EventServiceImpl implements
        CreateEventUseCase,
        GetEventUseCase,
        GetAllEventsUseCase,
        UpdateEventUseCase,
        DeleteEventUseCase {

    // Inyecta SÓLO los Puertos de Salida (Persistencia)
    private final EventRepositoryPort eventRepositoryPort;
    private final VenueRepositoryPort venueRepositoryPort;

    public EventServiceImpl(EventRepositoryPort eventRepositoryPort, VenueRepositoryPort venueRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.venueRepositoryPort = venueRepositoryPort;
    }

    //IMPLEMENTACIONES CRUD (TRABAJA CON MODELS)
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

        // El Puerto de Persistencia (EventJpaAdapter) se encarga del mapeo a Entity
        return eventRepositoryPort.save(eventModel);
    }

    @Override
    public EventModel getById(Long id) {
        return eventRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
    }

    @Override
    public Page<EventModel> getAll(Pageable pageable, String city, LocalDate date) {
        // El Use Case solo delega la búsqueda y paginación al Puerto
        return eventRepositoryPort.findAll(pageable, city, date);
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

        // Guardar
        return eventRepositoryPort.save(eventModel);
    }

    @Override
    public void delete(Long id) {
        if (eventRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }
        eventRepositoryPort.deleteById(id);
    }
}