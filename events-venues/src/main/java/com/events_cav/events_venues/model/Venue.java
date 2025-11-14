package com.events_cav.events_venues.model;

import jakarta.validation.constraints.NotBlank;

public record Venue(
        Long id_venue,
        String name,
        String location
) {}