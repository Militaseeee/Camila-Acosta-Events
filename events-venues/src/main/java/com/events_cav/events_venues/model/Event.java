package com.events_cav.events_venues.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Event(
    Long id_event,
    String name,
    String date,
    Long id_venue
) {}
