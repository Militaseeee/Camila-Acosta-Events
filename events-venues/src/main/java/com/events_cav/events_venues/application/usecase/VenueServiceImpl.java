package com.events_cav.events_venues.application.usecase;

import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.input.*;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
// Implementa las 5 interfaces de Use Case (Puertos de Entrada)
public class VenueServiceImpl implements
        CreateVenueUseCase,
        GetVenueUseCase,
        GetAllVenuesUseCase,
        UpdateVenueUseCase,
        DeleteVenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public VenueServiceImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    // IMPLEMENTACIONES CRUD (TRABAJA CON MODELS)
    @Override
    public VenueModel create(VenueModel model) {
        // Validación de Duplicados
        if (venueRepositoryPort.existsByName(model.getName())) {
            throw new ResourceConflictException("A venue with name '" + model.getName() + "' already exists");
        }

        // Guardar (El Puerto de Persistencia se encarga del mapeo a Entity)
        return venueRepositoryPort.save(model);
    }

    @Override
    public VenueModel getById(Long id) {
        return venueRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));
    }

    @Override
    public List<VenueModel> getAll() {
        return venueRepositoryPort.findAll();
    }

    @Override
    public VenueModel update(Long id, VenueModel model) {
        // Verificar existencia
        if (venueRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Venue not found with ID: " + id);
        }

        // Validar nombre duplicado
        if (venueRepositoryPort.existsByNameAndIdNot(model.getName(), id)) {
            throw new ResourceConflictException("A venue with name '" + model.getName() + "' already exists");
        }

        // Asignar ID al modelo de entrada
        model.setId(id);

        // Guardar
        return venueRepositoryPort.save(model);
    }

    @Override
    public void delete(Long id) {
        if (venueRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete. Venue not found with ID: " + id);
        }
        venueRepositoryPort.deleteById(id);
    }
}