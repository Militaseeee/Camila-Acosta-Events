package com.events_cav.events_venues.dto;

public record EventResponse(
        Long id,
        String name,
        String date,
        VenueResponse venue
) {}
