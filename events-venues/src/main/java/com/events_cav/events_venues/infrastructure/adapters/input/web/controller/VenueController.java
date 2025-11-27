package com.events_cav.events_venues.infrastructure.adapters.input.web.controller;

import com.events_cav.events_venues.domain.model.VenueModel;
// Importamos las 5 interfaces de Use Case (Puertos de Entrada)
import com.events_cav.events_venues.domain.ports.input.venue.CreateVenueUseCase;
import com.events_cav.events_venues.domain.ports.input.venue.GetVenueUseCase;
import com.events_cav.events_venues.domain.ports.input.venue.GetAllVenuesUseCase;
import com.events_cav.events_venues.domain.ports.input.venue.UpdateVenueUseCase;
import com.events_cav.events_venues.domain.ports.input.venue.DeleteVenueUseCase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.VenueRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.VenueResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.VenueMapper;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Imports de Swagger / OpenAPI
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

    // Declaración de las 5 dependencias de Casos de Uso (Puertos de Entrada)
    private final CreateVenueUseCase createVenueUseCase;
    private final GetVenueUseCase getVenueUseCase;
    private final GetAllVenuesUseCase getAllVenuesUseCase;
    private final UpdateVenueUseCase updateVenueUseCase;
    private final DeleteVenueUseCase deleteVenueUseCase;

    // private final VenueMapper venueMapper = VenueMapper.INSTANCE;
    // inyecto el Mapper en el constructor para consistencia
    private final VenueMapper venueMapper;

    // Constructor con Inyección de Dependencias
    public VenueController(
            CreateVenueUseCase createVenueUseCase,
            GetVenueUseCase getVenueUseCase,
            GetAllVenuesUseCase getAllVenuesUseCase,
            UpdateVenueUseCase updateVenueUseCase,
            DeleteVenueUseCase deleteVenueUseCase, VenueMapper venueMapper) {
        this.createVenueUseCase = createVenueUseCase;
        this.getVenueUseCase = getVenueUseCase;
        this.getAllVenuesUseCase = getAllVenuesUseCase;
        this.updateVenueUseCase = updateVenueUseCase;
        this.deleteVenueUseCase = deleteVenueUseCase;
        this.venueMapper = venueMapper;
    }

    // CREATE (usa CreateVenueUseCase)
    @Operation(summary = "Create a new Venue", description = "Creates a new venue in the system. The name must be unique.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Venue details to create",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                                "name": "New Convention Center", 
                                "location": "150 Main Street",
                                "city": "Bogotá", 
                                "capacity": 5000 
                            }
                        """))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venue created successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1, 
                                        "name": "New Convention Center", 
                                        "location": "150 Main Street",
                                        "city": "Bogotá", 
                                        "capacity": 5000 
                                    }
                                """))),
            @ApiResponse(responseCode = "409", description = "Duplicate venue name (Conflict)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"A venue with name 'New Convention Center' already exists\" }")))
    })
    @PostMapping
    public ResponseEntity<VenueResponse> create(@Valid @RequestBody VenueRequest request) {
        // ... Lógica de creación
        VenueModel modelToCreate = venueMapper.toVenueModel(request);
        VenueModel createdModel = createVenueUseCase.create(modelToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(venueMapper.toVenueResponse(createdModel));
    }

    // GET by ID (usa GetVenueUseCase)
    @Operation(summary = "Get Venue by ID", description = "Retrieves detailed information about a specific venue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1, 
                                        "name": "New Convention Center", 
                                        "location": "150 Main Street",
                                        "city": "Bogotá", 
                                        "capacity": 5000 
                                    }
                                """))),
            @ApiResponse(responseCode = "404", description = "Venue not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Venue not found with ID: 99\" }")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getById(@PathVariable Long id) {
        // ... Lógica de lectura
        VenueModel model = getVenueUseCase.getById(id);
        return ResponseEntity.ok(venueMapper.toVenueResponse(model));
    }

    // GET ALL (usa GetAllVenuesUseCase)
    @Operation(summary = "Get all Venues", description = "Retrieves a list of all registered venues.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    [
                                        { "id": 1, "name": "New Convention Center", "location": "150 Main Street", "city": "Bogotá", "capacity": 5000 },
                                        { "id": 2, "name": "Small Hall", "location": "North Ave", "city": "Medellín", "capacity": 500 }
                                    ]
                                """)))
    })
    @Parameter(name = "page", description = "Número de página (0..N)", example = "0")
    @Parameter(name = "size", description = "Número de registros por página", example = "10")
    @Parameter(name = "sort", description = "Criterio de ordenamiento: campo,(asc|desc). Ejemplo: name,asc", example = "name,asc")
    @GetMapping
    public ResponseEntity<Page<VenueResponse>> getAll(Pageable pageable) {
        Page<VenueModel> modelsPage = getAllVenuesUseCase.getAll(pageable);

        Page<VenueResponse> responsePage = modelsPage.map(venueMapper::toVenueResponse);

        return ResponseEntity.ok(responsePage);
    }

    // UPDATE (usa UpdateVenueUseCase)
    @Operation(summary = "Update a Venue", description = "Updates an existing venue by ID. The name must remain unique.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated venue details",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                                "name": "Updated Convention Center", 
                                "location": "200 Main Street",
                                "city": "Bogotá", 
                                "capacity": 6000 
                            }
                        """))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue updated successfully"),
            @ApiResponse(responseCode = "404", description = "Venue not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate name (Conflict)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"A venue with name 'Other Venue Name' already exists.\" }")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VenueRequest request) {
        // ... Lógica de actualización
        VenueModel modelToUpdate = venueMapper.toVenueModel(request);
        VenueModel updatedModel = updateVenueUseCase.update(id, modelToUpdate);
        return ResponseEntity.ok(venueMapper.toVenueResponse(updatedModel));
    }

    // DELETE (usa DeleteVenueUseCase)
    @Operation(summary = "Delete a Venue", description = "Removes a venue from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venue deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Venue not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Venue not found with ID: 99\" }")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // ... Lógica de eliminación
        deleteVenueUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
