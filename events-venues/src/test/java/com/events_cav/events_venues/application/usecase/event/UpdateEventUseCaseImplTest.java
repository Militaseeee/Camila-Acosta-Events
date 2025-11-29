package com.events_cav.events_venues.application.usecase.event;

import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateEventUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;
    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @InjectMocks
    private UpdateEventUseCaseImpl updateEventUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private VenueModel createVenue(Long id, String name) {
        return new VenueModel(id, name, "City", "Addr", 1000);
    }

    private EventModel createEvent(Long id, Long venueId, String name) {
        VenueModel venue = createVenue(venueId, "Venue");
        // ASUMIMOS DURACIÓN DE 1 DÍA PARA PRUEBAS
        return new EventModel(
                id,
                name,
                LocalDate.of(2026, 12, 1), // startDate
                LocalDate.of(2026, 12, 2), // endDate
                venue
        );
    }

    @Test
    void updateEvent_Success() {
        // ARRANGE
        Long eventId = 1L;
        Long venueId = 10L; // <-- El Venue al que se moverá/asignará
        VenueModel newVenue = createVenue(venueId, "New Venue");

        // Existing event debe usar el constructor de 5 args
        EventModel existingEvent = createEvent(eventId, 5L, "Old Concert");

        // CORRECCIÓN: new EventModel debe tener 5 argumentos
        EventModel updatedInput = new EventModel(
                eventId,
                "New Concert Name",
                LocalDate.of(2026, 12, 10), // Nuevo StartDate
                LocalDate.of(2026, 12, 11), // Nuevo EndDate (Añadido)
                newVenue
        );

        // Simular: 1. El Evento a actualizar existe
        when(eventRepositoryPort.findById(eventId)).thenReturn(Optional.of(existingEvent));
        // Simular: 2. El nuevo Venue existe
        when(venueRepositoryPort.findById(venueId)).thenReturn(Optional.of(newVenue));
        // Simular: 3. El nuevo nombre no tiene conflicto
        when(eventRepositoryPort.existsByNameAndIdNot(updatedInput.getName(), eventId)).thenReturn(false);
        // Simular: 4. El guardado es exitoso
        when(eventRepositoryPort.save(updatedInput)).thenReturn(updatedInput);

        // ACT
        // CORRECCIÓN: Pasar eventId, updatedInput, y venueId
        EventModel result = updateEventUseCase.update(eventId, updatedInput, venueId);

        // ASSERT
        assertNotNull(result);
        assertEquals("New Concert Name", result.getName());
        verify(eventRepositoryPort, times(1)).findById(eventId);
        verify(venueRepositoryPort, times(1)).findById(venueId);
        verify(eventRepositoryPort, times(1)).existsByNameAndIdNot("New Concert Name", eventId);
        verify(eventRepositoryPort, times(1)).save(updatedInput);
    }

    @Test
    void updateEvent_NotFound_ThrowsException() {
        Long eventId = 1L;
        Long venueId = 10L;
        VenueModel existingVenue = createVenue(venueId, "Venue");

        EventModel existingEvent = createEvent(eventId, venueId, "Old Name");
        EventModel updatedInput = createEvent(eventId, venueId, "Existing Name");

        // Simular: 1. El Evento existe
        when(eventRepositoryPort.findById(eventId)).thenReturn(Optional.of(existingEvent));
        // Simular: 2. El Venue existe
        when(venueRepositoryPort.findById(venueId)).thenReturn(Optional.of(existingVenue));
        // Simular: 3. El nuevo nombre *SÍ* está en uso por otro Evento
        when(eventRepositoryPort.existsByNameAndIdNot("Existing Name", eventId)).thenReturn(true);

        // ACT & ASSERT
        assertThrows(ResourceConflictException.class, () -> {
            // CORRECCIÓN: Pasar eventId, updatedInput, y venueId
            updateEventUseCase.update(eventId, updatedInput, venueId);
        });

        verify(eventRepositoryPort, times(1)).findById(eventId);
        verify(eventRepositoryPort, times(1)).existsByNameAndIdNot("Existing Name", eventId);
        verify(eventRepositoryPort, never()).save(any());
        verify(venueRepositoryPort, times(1)).findById(venueId);
    }

    @Test
    void updateEvent_NewVenueNotFound_ThrowsException() {
        // ARRANGE
        Long eventId = 1L;
        Long nonExistentVenueId = 99L;
        EventModel existingEvent = createEvent(eventId, 1L, "Existing"); // Evento base
        EventModel inputModel = createEvent(eventId, nonExistentVenueId, "Test");

        // Simular: 1. El Evento a actualizar existe
        when(eventRepositoryPort.findById(eventId)).thenReturn(Optional.of(existingEvent));
        // Simular: 2. El nuevo Venue NO existe
        when(venueRepositoryPort.findById(nonExistentVenueId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            // CORRECCIÓN: Pasar eventId, inputModel, y nonExistentVenueId
            updateEventUseCase.update(eventId, inputModel, nonExistentVenueId);
        });

        verify(eventRepositoryPort, times(1)).findById(eventId);
        verify(venueRepositoryPort, times(1)).findById(nonExistentVenueId);
        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    void updateEvent_DuplicateName_ThrowsConflictException() {
        // ARRANGE
        Long eventId = 1L;
        Long venueId = 10L;
        VenueModel existingVenue = createVenue(venueId, "Venue");

        EventModel existingEvent = createEvent(eventId, venueId, "Old Name");
        EventModel updatedInput = createEvent(eventId, venueId, "Existing Name");

        // Simular: 1. El Evento existe
        when(eventRepositoryPort.findById(eventId)).thenReturn(Optional.of(existingEvent));
        // Simular: 2. El Venue existe
        when(venueRepositoryPort.findById(venueId)).thenReturn(Optional.of(existingVenue));
        // Simular: 3. El nuevo nombre *SÍ* está en uso por otro Evento
        when(eventRepositoryPort.existsByNameAndIdNot("Existing Name", eventId)).thenReturn(true);

        // ACT & ASSERT
        assertThrows(ResourceConflictException.class, () -> {
            updateEventUseCase.update(eventId, updatedInput, venueId);
        });

        verify(eventRepositoryPort, times(1)).findById(eventId);
        verify(eventRepositoryPort, times(1)).existsByNameAndIdNot("Existing Name", eventId);
        verify(eventRepositoryPort, never()).save(any());
    }
}