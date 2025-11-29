package com.events_cav.events_venues.infrastructure.adapters.input.web.controller;

import com.events_cav.events_venues.domain.ports.input.user.AuthService;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.LoginRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.RegisterRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.TokenResponse;
import com.events_cav.events_venues.infrastructure.adapters.input.web.mapper.AuthRequestMapper;
import com.events_cav.events_venues.domain.model.user.UserRegisterCommand;
import com.events_cav.events_venues.domain.model.user.UserLoginCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthRequestMapper authRequestMapper;

    // Endpoint: /auth/register
    @Operation(summary = "Register New User",
            description = "Creates a new user in the system and encrypts their password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (e.g., password too short)"),
            @ApiResponse(responseCode = "409", description = "Username already exists (Conflict)")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

        // Mapear DTO de Infraestructura a Command de Dominio
        UserRegisterCommand command = authRequestMapper.toUserRegisterCommand(request);

        // Llamar al Puerto de Dominio con el Command
        authService.register(command);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint: /auth/login
    @Operation(summary = "User Login",
            description = "Authenticates the user with credentials and returns a JWT (Bearer Token)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication. Returns the JWT token.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class) // <-- Response DTO
                    )),
            @ApiResponse(responseCode = "401", description = "Invalid credentials (Unauthorized)"),
            @ApiResponse(responseCode = "500", description = "Internal server error (weak JWT key, etc.)")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        // Mapear DTO de Infraestructura a Command de Dominio
        UserLoginCommand command = authRequestMapper.toUserLoginCommand(request);

        // Llamar al Puerto de Dominio con el Command
        String jwt = authService.authenticate(command);

        return ResponseEntity.ok(new TokenResponse(jwt));
    }
}