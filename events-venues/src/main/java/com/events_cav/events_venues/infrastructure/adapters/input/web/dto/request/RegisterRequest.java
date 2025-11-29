package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request;

import com.events_cav.events_venues.domain.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO of application for the registration of new users")
public record RegisterRequest(
        @Schema(description = "Username or unique email address", example = "user@example.com")
        @NotNull(message = "The username is required")
        String username,

        @Schema(description = "Secure password (minimum 6 characters)", example = "MySecurePassword123")
        @NotNull(message = "A password is required")
        @Size(min = 6, message = "The password must be at least 6 characters long")
        String password,

        @Schema(description = "User role (ADMIN or USER)", example = "USER")
        @NotNull(message = "The role is mandatory")
        Role role
) {}