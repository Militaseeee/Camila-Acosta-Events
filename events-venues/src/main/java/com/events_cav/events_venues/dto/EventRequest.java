package com.events_cav.events_venues.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventRequest(
        @NotBlank(message = "The event name is required")
        String name,

        @NotBlank(message = "The date is mandatory")
        String date,

        @NotNull(message = "Venue ID is required")
        Long idVenue
) {}