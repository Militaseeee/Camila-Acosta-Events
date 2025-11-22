package com.events_cav.events_venues.domain.ports.input;

import com.events_cav.events_venues.domain.model.EventModel;

// Define el caso de uso para obtener un Evento por ID.
// Recibe: El ID del Evento.
// Retorna: El modelo de Evento.
public interface GetEventUseCase {
    EventModel getById(Long id);
}