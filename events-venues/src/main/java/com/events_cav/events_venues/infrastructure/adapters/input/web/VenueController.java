package com.events_cav.events_venues.infrastructure.adapters.input.web;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.VenueRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.VenueResponse;
import com.events_cav.events_venues.domain.ports.input.VenueManagementPort_yaNo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Importaciones de Swagger / OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueManagementPort_yaNo venueService;

    public VenueController(VenueManagementPort_yaNo venueService) {
        this.venueService = venueService;
    }

    // Create
    @Operation(summary = "Create a new Venue", description = "Creates a new venue in the database. Name must be unique")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Venue details to create",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"name\": \"Grand Hall\", \"location\": \"New York\" }")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venue created successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"name\": \"Grand Hall\", \"location\": \"New York\" }"))),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate name",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"A venue with name 'Grand Hall' already exists.\" }")))
    })
    @PostMapping
    public ResponseEntity<VenueResponse> create(@Valid @RequestBody VenueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.create(request));
    }

    // Get by ID
    @Operation(summary = "Get Venue by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"name\": \"Grand Hall\", \"location\": \"New York\" }"))),
            @ApiResponse(responseCode = "404", description = "Venue not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Venue not found with ID: 1\" }")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getById(id));
    }

    // Get All
    @Operation(summary = "Get all Venues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of venues retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    { "id": 1, "name": "Grand Hall", "location": "New York" },
                                    { "id": 2, "name": "Tech Center", "location": "San Francisco" }
                                ]
                            """)))
    })
    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAll() {
        return ResponseEntity.ok(venueService.getAll());
    }

    // Update
    @Operation(summary = "Update a Venue", description = "Updates an existing venue by ID")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated venue details",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"name\": \"Grand Hall Updated\", \"location\": \"New Jersey\" }")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue updated successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"name\": \"Grand Hall Updated\", \"location\": \"New Jersey\" }"))),
            @ApiResponse(responseCode = "404", description = "Venue not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Venue not found with ID: 99\" }"))),
            @ApiResponse(responseCode = "400", description = "Invalid data or duplicate name",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"A venue with name 'Grand Hall Updated' already exists\" }")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VenueRequest request) {
        return ResponseEntity.ok(venueService.update(id, request));
    }

    // Delete
    @Operation(summary = "Delete a Venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venue deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Venue not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Venue not found with ID: 1\" }")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}