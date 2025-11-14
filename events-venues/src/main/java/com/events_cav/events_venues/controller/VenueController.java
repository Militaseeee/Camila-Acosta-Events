package com.events_cav.events_venues.controller;

import com.events_cav.events_venues.dto.VenueRequest;
import com.events_cav.events_venues.dto.VenueResponse;
import com.events_cav.events_venues.service.interfaces.IVenueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final IVenueService venueService;

    public VenueController(IVenueService venueService) {
        this.venueService = venueService;
    }

    // Create
    @PostMapping
    public ResponseEntity<VenueResponse> create(@Valid @RequestBody VenueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.create(request));
    }

    // Search by ID
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getById(id));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAll() {
        return ResponseEntity.ok(venueService.getAll());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VenueRequest request) {
        return ResponseEntity.ok(venueService.update(id, request));
    }

    // Delte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build(); // return 204 No Content
    }
}