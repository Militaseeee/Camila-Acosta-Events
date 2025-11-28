package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String username,
        @Size(min = 6) String password,
        @Pattern(regexp = "ADMIN|USER") String role // Asegura que el rol sea uno válido
) {}