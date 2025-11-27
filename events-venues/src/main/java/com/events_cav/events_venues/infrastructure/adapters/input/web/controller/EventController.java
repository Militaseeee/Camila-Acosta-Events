package com.events_cav.events_venues.infrastructure.adapters.input.web.controller;

import com.events_cav.events_venues.domain.model.EventModel;
// Importamos las 5 interfaces de Use Case (Puertos de Entrada)
import com.events_cav.events_venues.domain.ports.input.event.CreateEventUseCase;
import com.events_cav.events_venues.domain.ports.input.event.GetEventUseCase;
import com.events_cav.events_venues.domain.ports.input.event.GetAllEventsUseCase;
import com.events_cav.events_venues.domain.ports.input.event.UpdateEventUseCase;
import com.events_cav.events_venues.domain.ports.input.event.DeleteEventUseCase;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.EventRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.EventResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.EventMapper;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Contiene el @RequestBody correcto de Spring

// Imports de Swagger (documentación)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
// 🛑 CORRECCIÓN CLAVE: Eliminamos el import conflictivo de Swagger RequestBody
// import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.time.LocalDate;

@RestController
@RequestMapping("/events")
public class EventController {

    // Declaración de las 5 dependencias de Casos de Uso (Puertos de Entrada)
    private final CreateEventUseCase createEventUseCase;
    private final GetEventUseCase getEventUseCase;
    private final GetAllEventsUseCase getAllEventsUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;

    private final EventMapper eventMapper;

    // Constructor con Inyección de Dependencias
    public EventController(
            CreateEventUseCase createEventUseCase,
            GetEventUseCase getEventUseCase,
            GetAllEventsUseCase getAllEventsUseCase,
            UpdateEventUseCase updateEventUseCase,
            DeleteEventUseCase deleteEventUseCase,
            EventMapper eventMapper) {
        this.createEventUseCase = createEventUseCase;
        this.getEventUseCase = getEventUseCase;
        this.getAllEventsUseCase = getAllEventsUseCase;
        this.updateEventUseCase = updateEventUseCase;
        this.deleteEventUseCase = deleteEventUseCase;
        this.eventMapper = eventMapper;
    }

    // CREATE (usa CreateEventUseCase)
    @Operation(summary = "Create a new Event", description = "Creates a new event associated with an existing venue. The name must be unique.")
    // ✅ CORRECCIÓN: Usamos el FQN (Fully Qualified Name) de Swagger para la documentación
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Event details to create (requires valid Venue ID)",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                                "name": "Rock Festival 2026",
                                "date": "2026-11-20",
                                "idVenue": 1
                            }
                        """))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "name": "Rock Festival 2026",
                                        "date": "2026-11-20",
                                        "venue": {
                                            "id": 1,
                                            "name": "Movistar Arena",
                                            "location": "Bogotá",
                                            "city": "Bogotá",
                                            "capacity": 14000
                                        }
                                    }
                                """))),
            @ApiResponse(responseCode = "404", description = "Venue ID does not exist",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"The Venue with ID 99 does not exist\" }"))),
            @ApiResponse(responseCode = "409", description = "Duplicate event name (Conflict)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"An event with name 'Rock Festival 2026' already exists\" }")))
    })
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) { // Usa el @RequestBody de Spring
        // ... Lógica de creación
        EventModel modelToCreate = eventMapper.toEventModel(request);
        EventModel createdModel = createEventUseCase.create(modelToCreate, request.getIdVenue());
        return ResponseEntity.status(HttpStatus.CREATED).body(eventMapper.toEventResponse(createdModel));
    }

    // GET by ID (usa GetEventUseCase)
    @Operation(summary = "Get Event by ID", description = "Retrieves detailed information about a specific event, including its venue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "name": "Rock Festival 2026",
                                        "date": "2026-11-20",
                                        "venue": {
                                            "id": 1,
                                            "name": "Movistar Arena",
                                            "location": "Bogotá",
                                            "city": "Bogotá",
                                            "capacity": 14000
                                        }
                                    }
                                """))),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Event not found with ID: 99\" }")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        // ... Lógica de lectura
        EventModel model = getEventUseCase.getById(id);
        return ResponseEntity.ok(eventMapper.toEventResponse(model));
    }

    // GET ALL (usa GetAllEventsUseCase)
    @Operation(summary = "Get all Events with Pagination and Filters", description = "Retrieves a paginated list of all events, optionally filtering by city and date. Solves N+1 problem.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated list retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDate date
    ) {
        // Delegación al Caso de Uso específico para la consulta paginada
        Page<EventModel> modelsPage = getAllEventsUseCase.getAll(pageable, city, date);
        return ResponseEntity.ok(modelsPage.map(eventMapper::toEventResponse));
    }

    // UPDATE (usa UpdateEventUseCase)
    @Operation(summary = "Update an Event", description = "Updates an existing event's information by ID. Requires a valid Venue ID.")
    // ✅ CORRECCIÓN: Usamos el FQN (Fully Qualified Name) de Swagger para la documentación
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated event details",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                                "name": "Updated Festival 2026",
                                "date": "2026-12-01",
                                "idVenue": 1
                            }
                        """))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "404", description = "Event or Venue not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate name (Conflict)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) { // Usa el @RequestBody de Spring
        // ... Lógica de actualización
        EventModel modelToUpdate = eventMapper.toEventModel(request);
        EventModel updatedModel = updateEventUseCase.update(id, modelToUpdate, request.getIdVenue());
        return ResponseEntity.ok(eventMapper.toEventResponse(updatedModel));
    }

    // DELETE (usa DeleteEventUseCase)
    @Operation(summary = "Delete an Event", description = "Removes an event from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Event not found with ID: 99\" }")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // ... Lógica de eliminación
        deleteEventUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}