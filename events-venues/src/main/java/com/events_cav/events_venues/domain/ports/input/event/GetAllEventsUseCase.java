package com.events_cav.events_venues.domain.ports.input.event;

import com.events_cav.events_venues.domain.model.EventModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

// Define el caso de uso para listar Eventos con paginación y filtros.
// Recibe: Pageable de Spring (dependencia de framework), filtros.
// Retorna: Página de modelos de Evento.
public interface GetAllEventsUseCase {
    Page<EventModel> getAll(Pageable pageable, String city, LocalDate date);
}