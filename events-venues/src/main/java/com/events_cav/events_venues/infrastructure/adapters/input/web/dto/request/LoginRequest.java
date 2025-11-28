package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request;

public record LoginRequest(
        String username,
        String password
) {}