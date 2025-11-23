package com.events_cav.events_venues.infrastructure.adapters.input.web;

import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.ports.input.*; // Importa los 5 Use Cases
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.EventRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.EventResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.EventMapper; // Importa el Mapper
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Imports de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@RestController
@RequestMapping("/events")
public class EventController {

    // Inyección de los 5 Use Cases
    private final CreateEventUseCase createEventUseCase;
    private final GetEventUseCase getEventUseCase;
    private final GetAllEventsUseCase getAllEventsUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;

    // Inyección del Mapper (Responsabilidad del Adaptador)
    private final EventMapper eventMapper = EventMapper.INSTANCE;

    // Constructor con todas las inyecciones de Use Cases
    public EventController(
            CreateEventUseCase createEventUseCase,
            GetEventUseCase getEventUseCase,
            GetAllEventsUseCase getAllEventsUseCase,
            UpdateEventUseCase updateEventUseCase,
            DeleteEventUseCase deleteEventUseCase) {
        this.createEventUseCase = createEventUseCase;
        this.getEventUseCase = getEventUseCase;
        this.getAllEventsUseCase = getAllEventsUseCase;
        this.updateEventUseCase = updateEventUseCase;
        this.deleteEventUseCase = deleteEventUseCase;
    }

    // Create Event
    @Operation(summary = "Create a new Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "404", description = "Venue ID does not exist"),
            @ApiResponse(responseCode = "409", description = "Duplicate event name (Conflict)")
    })
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        // DTO -> Model
        EventModel modelToCreate = eventMapper.toEventModel(request);

        // Llamar al Use Case
        EventModel createdModel = createEventUseCase.create(modelToCreate, request.getIdVenue());

        // Model -> DTO Response
        return ResponseEntity.status(HttpStatus.CREATED).body(eventMapper.toEventResponse(createdModel));
    }

    // Get Event by ID
    @Operation(summary = "Get Event by ID")
    @ApiResponses(value = { /* Swagger Responses */
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        // Llamar al Use Case
        EventModel model = getEventUseCase.getById(id);

        // Model -> DTO Response
        return ResponseEntity.ok(eventMapper.toEventResponse(model));
    }

    // Get All Events (Paginación y Filtros)
    @Operation(summary = "Get all Events with Pagination and Filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated list retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate date
    ) {
        // Llamar al Use Case (devuelve Page<Model>)
        Page<EventModel> modelsPage = getAllEventsUseCase.getAll(pageable, city, date);

        // Mapear Page<Model> a Page<Response DTO>
        return ResponseEntity.ok(modelsPage.map(eventMapper::toEventResponse));
    }

    // Update Event
    @Operation(summary = "Update an Event")
    @ApiResponses(value = { /* Swagger Responses */
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "404", description = "Event or Venue not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate name (Conflict)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        // DTO -> Model
        EventModel modelToUpdate = eventMapper.toEventModel(request);

        // Llamar al Use Case
        EventModel updatedModel = updateEventUseCase.update(id, modelToUpdate, request.getIdVenue());

        // Model -> DTO Response
        return ResponseEntity.ok(eventMapper.toEventResponse(updatedModel));
    }

    // Delete Event
    @Operation(summary = "Delete an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteEventUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}