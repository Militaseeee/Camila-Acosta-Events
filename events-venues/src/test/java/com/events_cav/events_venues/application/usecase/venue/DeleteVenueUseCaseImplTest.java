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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteVenueUseCaseImplTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort; // Simula el repositorio

    @InjectMocks
    private DeleteVenueUseCaseImpl deleteVenueUseCase; // La clase a probar

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteVenueById_Success() {
        // ARRANGE
        Long venueId = 1L;
        // Simular: findById() encuentra el Venue
        when(venueRepositoryPort.findById(venueId)).thenReturn(Optional.of(mock(VenueModel.class)));
        // Simular: el borrado (método void) no hace nada
        doNothing().when(venueRepositoryPort).deleteById(venueId);

        // ACT
        deleteVenueUseCase.delete(venueId);

        // ASSERT
        // Verificar que la existencia se chequeó y el borrado se ejecutó
        verify(venueRepositoryPort, times(1)).findById(venueId);
        verify(venueRepositoryPort, times(1)).deleteById(venueId);
    }

    @Test
    void deleteVenueById_NotFound_ThrowsException() {
        // ARRANGE
        Long nonExistentId = 99L;
        // Simular: findById() NO encuentra el Venue
        when(venueRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            deleteVenueUseCase.delete(nonExistentId);
        });

        // ASSERT
        verify(venueRepositoryPort, times(1)).findById(nonExistentId);
        // Verificar que el método deleteById NUNCA fue llamado
        verify(venueRepositoryPort, never()).deleteById(anyLong());
    }
}