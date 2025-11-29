package com.events_cav.events_venues.application.usecase.venue;

import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetVenueUseCaseImplTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort; // Simula el repositorio

    @InjectMocks
    private GetVenueUseCaseImpl getVenueUseCase; // La clase a probar

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVenueById_Success() {
        // ARRANGE: Preparar datos
        Long venueId = 1L;
        VenueModel expectedModel = new VenueModel(
                venueId, "Arena Test", "Test City", "123 Test St", 10000
        );
        // Simular: El repositorio encuentra el Venue
        when(venueRepositoryPort.findById(venueId)).thenReturn(Optional.of(expectedModel));

        // ACT: Ejecutar
        VenueModel result = getVenueUseCase.getById(venueId);

        // ASSERT: Verificar
        assertNotNull(result);
        assertEquals(venueId, result.getId());
        verify(venueRepositoryPort, times(1)).findById(venueId);
    }

    @Test
    void getVenueById_NotFound_ThrowsException() {
        // ARRANGE: ID que no existe
        Long nonExistentId = 99L;
        // Simular: El repositorio NO encuentra el Venue
        when(venueRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        // ACT & ASSERT: Verificar que se lance la excepción correcta
        assertThrows(ResourceNotFoundException.class, () -> {
            getVenueUseCase.getById(nonExistentId);
        });

        verify(venueRepositoryPort, times(1)).findById(nonExistentId);
    }
}