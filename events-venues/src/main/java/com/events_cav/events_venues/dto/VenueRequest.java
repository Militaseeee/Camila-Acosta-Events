package com.events_cav.events_venues.dto;

import jakarta.validation.constraints.NotBlank;

// Create and Update
public record VenueRequest(
        @NotBlank(message = "The venue name is required")
        String name,

        @NotBlank(message = "Location is mandatory")
        String location
) {}