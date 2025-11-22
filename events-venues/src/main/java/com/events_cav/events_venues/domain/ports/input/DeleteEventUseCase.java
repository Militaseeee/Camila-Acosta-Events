package com.events_cav.events_venues.domain.ports.input;

// Define el caso de uso para eliminar un Evento.
// Recibe: El ID del Evento.
public interface DeleteEventUseCase {
    void delete(Long id);
}