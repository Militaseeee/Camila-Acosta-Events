package com.events_cav.events_venues.infrastructure.adapters.input.web;

import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.input.*; // Importa los 5 Use Cases
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.VenueRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.VenueResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.VenueMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Imports de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venues")
public class VenueController {

    // Inyección de los 5 Use Cases
    private final CreateVenueUseCase createVenueUseCase;
    private final GetVenueUseCase getVenueUseCase;
    private final GetAllVenuesUseCase getAllVenuesUseCase;
    private final UpdateVenueUseCase updateVenueUseCase;
    private final DeleteVenueUseCase deleteVenueUseCase;

    // Inyección del Mapper (Responsabilidad del Adaptador)
    private final VenueMapper venueMapper = VenueMapper.INSTANCE;

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

    // Create
    @Operation(summary = "Create a new Venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venue created successfully"),
            @ApiResponse(responseCode = "409", description = "Duplicate venue name (Conflict)")
    })
    @PostMapping
    public ResponseEntity<VenueResponse> create(@Valid @RequestBody VenueRequest request) {
        // DTO -> Model
        VenueModel modelToCreate = venueMapper.toVenueModel(request);

        // Llamar al Use Case
        VenueModel createdModel = createVenueUseCase.create(modelToCreate);

        // Model -> DTO Response
        return ResponseEntity.status(HttpStatus.CREATED).body(venueMapper.toVenueResponse(createdModel));
    }

    // Get by ID
    @Operation(summary = "Get Venue by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue found"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getById(@PathVariable Long id) {
        // Llamar al Use Case
        VenueModel model = getVenueUseCase.getById(id);

        // Model -> DTO Response
        return ResponseEntity.ok(venueMapper.toVenueResponse(model));
    }

    // Get All
    @Operation(summary = "Get all Venues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of venues retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAll() {
        // Llamar al Use Case
        List<VenueModel> models = getAllVenuesUseCase.getAll();

        // Mapear List<Model> -> List<Response DTO>
        List<VenueResponse> responses = models.stream()
                .map(venueMapper::toVenueResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Update
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
        // DTO -> Model
        VenueModel modelToUpdate = venueMapper.toVenueModel(request);

        // Llamar al Use Case
        VenueModel updatedModel = updateVenueUseCase.update(id, modelToUpdate);

        // Model -> DTO Response
        return ResponseEntity.ok(venueMapper.toVenueResponse(updatedModel));
    }

    // Delete
    @Operation(summary = "Delete a Venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venue deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteVenueUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}