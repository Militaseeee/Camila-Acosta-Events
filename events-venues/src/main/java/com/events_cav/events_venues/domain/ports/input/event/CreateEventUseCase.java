package com.events_cav.events_venues.domain.ports.input.event;

import com.events_cav.events_venues.domain.model.EventModel;

// Define el caso de uso para la creación de un Evento
// Recibe: El modelo de Evento (puro dominio) y el ID del Venue (dependencia)
// Retorna: El modelo de Evento creado
public interface CreateEventUseCase {
    EventModel create(EventModel event, Long venueId);
}