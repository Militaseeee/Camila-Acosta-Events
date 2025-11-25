package com.events_cav.events_venues.infrastructure.adapters.input.web.controller;

import com.events_cav.events_venues.domain.model.VenueModel;
// Importamos las 5 interfaces de Use Case (Puertos de Entrada)
import com.events_cav.events_venues.domain.ports.input.venue.CreateVenueUseCase;
import com.events_cav.events_venues.domain.ports.input.venue.GetVenueUseCase;
import com.events_cav.events_venues.domain.ports.input.venue.GetAllVenuesUseCase;
import com.events_cav.events_venues.domain.ports.input.venue.UpdateVenueUseCase;
import com.events_cav.events_venues.domain.ports.input.venue.DeleteVenueUseCase;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.VenueRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.VenueResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.VenueMapper;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venues")
public class VenueController {

    // Declaración de las 5 dependencias de Casos de Uso (Puertos de Entrada)
    private final CreateVenueUseCase createVenueUseCase;
    private final GetVenueUseCase getVenueUseCase;
    private final GetAllVenuesUseCase getAllVenuesUseCase;
    private final UpdateVenueUseCase updateVenueUseCase;
    private final DeleteVenueUseCase deleteVenueUseCase;

    private final VenueMapper venueMapper = VenueMapper.INSTANCE;

    // Constructor con Inyección de Dependencias
    public VenueController(
            CreateVenueUseCase createVenueUseCase,
            GetVenueUseCase getVenueUseCase,
            GetAllVenuesUseCase getAllVenuesUseCase,
            UpdateVenueUseCase updateVenueUseCase,
            DeleteVenueUseCase deleteVenueUseCase) {
        this.createVenueUseCase = createVenueUseCase;
        this.getVenueUseCase = getVenueUseCase;
        this.getAllVenuesUseCase = getAllVenuesUseCase;
        this.updateVenueUseCase = updateVenueUseCase;
        this.deleteVenueUseCase = deleteVenueUseCase;
    }

    // CREATE (usa CreateVenueUseCase)
    @Operation(summary = "Create a new Venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venue created successfully"),
            @ApiResponse(responseCode = "409", description = "Duplicate venue name (Conflict)")
    })
    @PostMapping
    public ResponseEntity<VenueResponse> create(@Valid @RequestBody VenueRequest request) {
        VenueModel modelToCreate = venueMapper.toVenueModel(request);

        // Delegación al Caso de Uso específico para la creación
        VenueModel createdModel = createVenueUseCase.create(modelToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(venueMapper.toVenueResponse(createdModel));
    }

    // GET by ID (usa GetVenueUseCase)
    @Operation(summary = "Get Venue by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue found"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getById(@PathVariable Long id) {
        // Delegación al Caso de Uso específico para la lectura
        VenueModel model = getVenueUseCase.getById(id);

        return ResponseEntity.ok(venueMapper.toVenueResponse(model));
    }

    // GET ALL (usa GetAllVenuesUseCase)
    @Operation(summary = "Get all Venues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAll() {
        // Delegación al Caso de Uso específico para la consulta
        List<VenueModel> models = getAllVenuesUseCase.getAll();

        // Mapeo de List<Model> a List<Response DTO>
        List<VenueResponse> responses = models.stream()
                .map(venueMapper::toVenueResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // UPDATE (usa UpdateVenueUseCase)
    @Operation(summary = "Update a Venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue updated successfully"),
            @ApiResponse(responseCode = "404", description = "Venue not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate name (Conflict)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VenueRequest request) {
        VenueModel modelToUpdate = venueMapper.toVenueModel(request);

        // Delegación al Caso de Uso específico para la actualización
        VenueModel updatedModel = updateVenueUseCase.update(id, modelToUpdate);

        return ResponseEntity.ok(venueMapper.toVenueResponse(updatedModel));
    }

    // DELETE (usa DeleteVenueUseCase)
    @Operation(summary = "Delete a Venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venue deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Delegación al Caso de Uso específico para la eliminación
        deleteVenueUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}