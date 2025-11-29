package com.events_cav.events_venues.application.usecase.event;

import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllEventsUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @InjectMocks
    private GetAllEventsUseCaseImpl getAllEventsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Utilidad: Refleja el constructor de 5 argumentos (ID, Nombre, StartDate, EndDate, Venue)
    private EventModel createTestEvent(Long id, String name, String city) {
        VenueModel venue = new VenueModel(1L, "Venue " + id, city, "Addr", 1000);
        return new EventModel(
                id,
                name,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 2),
                venue
        );
    }

    @Test
    void findAllEvents_NoFilters_ReturnsPagedResult() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        List<EventModel> eventList = Collections.singletonList(createTestEvent(1L, "Concert", "Miami"));
        Page<EventModel> expectedPage = new PageImpl<>(eventList, pageable, 1);

        // Simular: el repositorio devuelve una página sin filtros
        when(eventRepositoryPort.findAll(pageable, null, null, null)).thenReturn(expectedPage);

        // ACT
        Page<EventModel> result = getAllEventsUseCase.getAll(pageable, null, null, null);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        // Verificar que el repositorio fue llamado correctamente
        verify(eventRepositoryPort, times(1)).findAll(pageable, null, null, null);
    }

    @Test
    void findAllEvents_WithCityFilter_ReturnsFilteredResult() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        String cityFilter = "Medellin";
        List<EventModel> filteredList = Collections.singletonList(createTestEvent(2L, "Festival", cityFilter));
        Page<EventModel> expectedPage = new PageImpl<>(filteredList, pageable, 1);

        // Simular: el repositorio devuelve la página filtrada
        when(eventRepositoryPort.findAll(pageable, cityFilter, null, null)).thenReturn(expectedPage);

        // ACT
        Page<EventModel> result = getAllEventsUseCase.getAll(pageable, cityFilter, null, null);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Medellin", result.getContent().getFirst().getVenue().getCity());

        // Verificar que el repositorio fue llamado con el filtro de ciudad
        verify(eventRepositoryPort, times(1)).findAll(pageable, cityFilter, null, null);
    }
}