package com.events_cav.events_venues.controller;

import com.events_cav.events_venues.dto.request.EventRequest;
import com.events_cav.events_venues.dto.response.EventResponse;
import com.events_cav.events_venues.service.interfaces.IEventService;
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
@RequestMapping("/events")
public class EventController {

    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    // Create Event
    @Operation(summary = "Create a new Event", description = "Creates a new event associated with an existing venue. The name must be unique")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Event details to create (requires valid Venue ID)",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                        {
                            "name": "Rock Festival 2025",
                            "date": "2025-11-20",
                            "idVenue": 5
                        }
                    """))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "name": "Rock Festival 2025",
                                    "date": "2025-11-20",
                                    "venue": {
                                        "id": 5,
                                        "name": "Grand Stadium",
                                        "location": "Main St 123"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "400", description = "Invalid input, duplicate name, or Venue ID does not exist",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"The Venue with ID 5 does not exist\" }")))
    })
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(request));
    }

    // Get Event by ID
    @Operation(summary = "Get Event by ID", description = "Retrieves detailed information about a specific event, including its venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "name": "Jazz Night",
                                    "date": "2025-06-15",
                                    "venue": {
                                        "id": 2,
                                        "name": "Blue Lounge",
                                        "location": "Downtown Avenue"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Event not found with ID: 1\" }")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    // Get All Events
    @Operation(summary = "Get all Events", description = "Retrieves a list of all registered events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of events retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "name": "Rock Festival 2025",
                                        "date": "2025-11-20",
                                        "venue": {
                                            "id": 5,
                                            "name": "Grand Stadium",
                                            "location": "Main St 123"
                                        }
                                    },
                                    {
                                        "id": 2,
                                        "name": "Tech Conference",
                                        "date": "2025-09-10",
                                        "venue": {
                                            "id": 3,
                                            "name": "Convention Center",
                                            "location": "Silicon Valley"
                                        }
                                    }
                                ]
                            """)))
    })
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll() {
        return ResponseEntity.ok(eventService.getAll());
    }

    // Update Event
    @Operation(summary = "Update an Event", description = "Updates an existing event's information. Requires a valid Venue ID")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated event details",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                        {
                            "name": "Updated Rock Festival",
                            "date": "2025-12-01",
                            "idVenue": 5
                        }
                    """))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "name": "Updated Rock Festival",
                                    "date": "2025-12-01",
                                    "venue": {
                                        "id": 5,
                                        "name": "Grand Stadium",
                                        "location": "Main St 123"
                                    }
                                }
                             """))),
            @ApiResponse(responseCode = "404", description = "Event or Venue not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Event not found with ID: 99\" }"))),
            @ApiResponse(responseCode = "400", description = "Invalid data or duplicate name",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"An event with name 'Updated Rock Festival' already exists\" }")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        return ResponseEntity.ok(eventService.update(id, request));
    }

    // Delete Event
    @Operation(summary = "Delete an Event", description = "Removes an event from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Event not found with ID: 10\" }")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}