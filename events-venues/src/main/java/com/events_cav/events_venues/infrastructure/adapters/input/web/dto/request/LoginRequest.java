package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for login request")
public record LoginRequest(
        @Schema(description = "Username or email address", example = "admin.test@example.com")
        @NotNull(message = "The username is required")
        String username,

        @Schema(description = "Password", example = "MySecurePassword123")
        @NotNull(message = "A password is required")
        String password
) {}