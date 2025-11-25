package com.events_cav.events_venues.domain.ports.input.event;

import com.events_cav.events_venues.domain.model.EventModel;

// Define el caso de uso para actualizar un Evento.
// Recibe: El ID del Evento y el nuevo modelo de Evento.
// Retorna: El modelo de Evento actualizado.
public interface UpdateEventUseCase {
    EventModel update(Long id, EventModel event, Long venueId);
}