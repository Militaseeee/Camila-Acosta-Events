package com.events_cav.events_venues.application.usecase.event;

import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetEventUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @InjectMocks
    private GetEventUseCaseImpl getEventUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private EventModel createTestEvent(Long id) {
        VenueModel venue = new VenueModel(1L, "Test Venue", "City", "Addr", 5000);
        return new EventModel(
                id,
                "Concert Test",
                LocalDate.of(2026, 12, 1),  // startDate
                LocalDate.of(2026, 12, 2),  // endDate (Añadido)
                venue
        );
    }

    @Test
    void getEventById_Success() { // (Mantener nombre del test, pero corregir ACT)
        // ARRANGE
        Long eventId = 1L;
        EventModel expectedModel = createTestEvent(eventId);
        when(eventRepositoryPort.findById(eventId)).thenReturn(Optional.of(expectedModel));

        // ACT
        EventModel result = getEventUseCase.getById(eventId);

        // ASSERT
        assertNotNull(result);
        assertEquals(eventId, result.getId());
        verify(eventRepositoryPort, times(1)).findById(eventId);
    }

    @Test
    void getEventById_NotFound_ThrowsException() { // (Mantener nombre del test, pero corregir ACT & ASSERT)
        // ARRANGE
        Long nonExistentId = 99L;
        when(eventRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            getEventUseCase.getById(nonExistentId);
        });

        verify(eventRepositoryPort, times(1)).findById(nonExistentId);
    }
}