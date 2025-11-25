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
import org.springframework.web.bind.annotation.*;

// Imports de Swagger (documentación)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    // El Mapper es parte del Adaptador de Infraestructura/Salida
    // pero se usa aquí para la conversión DTO <=> Model
    private final EventMapper eventMapper = EventMapper.INSTANCE;

    // Constructor con Inyección de Dependencias
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

    // CREATE (usa CreateEventUseCase)
    @Operation(summary = "Create a new Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "404", description = "Venue ID does not exist"),
            @ApiResponse(responseCode = "409", description = "Duplicate event name (Conflict)")
    })
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        // Mapeo de Request DTO a Domain Model
        EventModel modelToCreate = eventMapper.toEventModel(request);

        // Delegación al Caso de Uso específico para la creación
        EventModel createdModel = createEventUseCase.create(modelToCreate, request.getIdVenue());

        // Mapeo de Domain Model a Response DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(eventMapper.toEventResponse(createdModel));
    }

    // GET by ID (usa GetEventUseCase)
    @Operation(summary = "Get Event by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        // Delegación al Caso de Uso específico para la lectura
        EventModel model = getEventUseCase.getById(id);

        return ResponseEntity.ok(eventMapper.toEventResponse(model));
    }

    // GET ALL (usa GetAllEventsUseCase)
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
        // Delegación al Caso de Uso específico para la consulta paginada
        Page<EventModel> modelsPage = getAllEventsUseCase.getAll(pageable, city, date);

        // Mapear Page<Model> a Page<Response DTO>
        return ResponseEntity.ok(modelsPage.map(eventMapper::toEventResponse));
    }

    // UPDATE (usa UpdateEventUseCase)
    @Operation(summary = "Update an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "404", description = "Event or Venue not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate name (Conflict)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        // Mapeo de Request DTO a Domain Model
        EventModel modelToUpdate = eventMapper.toEventModel(request);

        // Delegación al Caso de Uso específico para la actualización
        EventModel updatedModel = updateEventUseCase.update(id, modelToUpdate, request.getIdVenue());

        // Mapeo de Domain Model a Response DTO
        return ResponseEntity.ok(eventMapper.toEventResponse(updatedModel));
    }

    // DELETE (usa DeleteEventUseCase)
    @Operation(summary = "Delete an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Delegación al Caso de Uso específico para la eliminación
        deleteEventUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}