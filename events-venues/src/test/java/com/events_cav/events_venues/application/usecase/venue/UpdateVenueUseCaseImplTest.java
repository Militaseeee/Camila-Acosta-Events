package com.events_cav.events_venues.application.usecase.venue;

import com.events_cav.events_venues.domain.exception.ResourceConflictException;
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

class UpdateVenueUseCaseImplTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @InjectMocks
    private UpdateVenueUseCaseImpl updateVenueUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private VenueModel createVenue(Long id, String name) {
        return new VenueModel(id, name, "City", "Addr", 1000);
    }

    @Test
    void updateVenue_Success() {
        // ARRANGE
        Long venueId = 1L;
        VenueModel existingVenue = createVenue(venueId, "Old Name");
        VenueModel updatedInput = createVenue(venueId, "New Name");

        // Simular: 1. El Venue a actualizar existe
        when(venueRepositoryPort.findById(venueId)).thenReturn(Optional.of(existingVenue));
        // Simular: 2. El nuevo nombre no está en uso por otro Venue
        when(venueRepositoryPort.existsByNameAndIdNot(updatedInput.getName(), venueId)).thenReturn(false);
        // Simular: 3. El guardado es exitoso
        when(venueRepositoryPort.save(updatedInput)).thenReturn(updatedInput);

        // ACT
        VenueModel result = updateVenueUseCase.update(venueId, updatedInput);

        // ASSERT
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(venueRepositoryPort, times(1)).findById(venueId);
        verify(venueRepositoryPort, times(1)).existsByNameAndIdNot("New Name", venueId);
        verify(venueRepositoryPort, times(1)).save(updatedInput);
    }

    @Test
    void updateVenue_NotFound_ThrowsException() {
        // ARRANGE
        Long nonExistentId = 99L;
        VenueModel inputModel = createVenue(nonExistentId, "Missing");

        // Simular: El Venue NO existe
        when(venueRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            updateVenueUseCase.update(nonExistentId, inputModel);
        });

        // Verificar que el guardado NUNCA fue llamado
        verify(venueRepositoryPort, times(1)).findById(nonExistentId);
        verify(venueRepositoryPort, never()).save(any(VenueModel.class));
    }

    @Test
    void updateVenue_DuplicateName_ThrowsConflictException() {
        // ARRANGE
        Long venueId = 1L;
        VenueModel existingVenue = createVenue(venueId, "Old Name");
        VenueModel updatedInput = createVenue(venueId, "Existing Name");

        // Simular: 1. El Venue existe
        when(venueRepositoryPort.findById(venueId)).thenReturn(Optional.of(existingVenue));
        // Simular: 2. El nuevo nombre *SÍ* está en uso por otro Venue
        when(venueRepositoryPort.existsByNameAndIdNot("Existing Name", venueId)).thenReturn(true);

        // ACT & ASSERT
        assertThrows(ResourceConflictException.class, () -> {
            updateVenueUseCase.update(venueId, updatedInput);
        });

        // Verificar que el guardado NUNCA fue llamado
        verify(venueRepositoryPort, times(1)).findById(venueId);
        verify(venueRepositoryPort, times(1)).existsByNameAndIdNot("Existing Name", venueId);
        verify(venueRepositoryPort, never()).save(any(VenueModel.class));
    }
}