package com.events_cav.events_venues.application.usecase.venue;

import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Este es el caso de uso que vamos a probar
class CreateVenueUseCaseImplTest {

    // Simula la dependencia del repositorio (Puerto de Salida)
    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    // Inyecta el mock en la clase que estamos probando
    @InjectMocks
    private CreateVenueUseCaseImpl createVenueUseCase;

    @BeforeEach
    void setUp() {
        // Inicializa los Mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createVenue_Success() {
        // ARRANGE: Preparación
        VenueModel inputModel = new VenueModel(
                null, "Arena Test", "Test City", "123 Test St", 10000
        );
        VenueModel savedModel = new VenueModel(
                1L, "Arena Test", "Test City", "123 Test St", 10000
        );

        // Comportamiento esperado:
        // 1. findByName no encuentra duplicados
        when(venueRepositoryPort.findByName(inputModel.getName())).thenReturn(Optional.empty());
        // 2. save retorna el modelo guardado
        when(venueRepositoryPort.save(inputModel)).thenReturn(savedModel);

        // ACT: Ejecución
        VenueModel result = createVenueUseCase.create(inputModel);

        // ASSERT: Verificación
        assertNotNull(result);
        assertEquals(1L, result.getId());

        // Verificar que los métodos del repositorio fueron llamados
        verify(venueRepositoryPort, times(1)).findByName(inputModel.getName());
        verify(venueRepositoryPort, times(1)).save(inputModel);
    }

    @Test
    void createVenue_DuplicateName_ThrowsConflictException() {
        // ARRANGE: Simular que ya existe un Venue con el mismo nombre
        VenueModel inputModel = new VenueModel(
                null, "Arena Test", "Test City", "123 Test St", 10000
        );

        // Comportamiento esperado: findByName encuentra el duplicado
        when(venueRepositoryPort.findByName(inputModel.getName())).thenReturn(Optional.of(inputModel));

        // ACT & ASSERT: Verificar que se lance la excepción correcta (ResourceConflictException)
        assertThrows(ResourceConflictException.class, () -> {
            createVenueUseCase.create(inputModel);
        });

        // Verificar que el metodo save NUNCA fue llamado
        verify(venueRepositoryPort, never()).save(any(VenueModel.class));
    }
}