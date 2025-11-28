// src/main/java/com/events_cav/events_venues/infrastructure/adapters/input/web/controller/AuthController.java
package com.events_cav.events_venues.infrastructure.adapters.input.web.controller;

import com.events_cav.events_venues.application.usecase.AuthService;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.LoginRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.RegisterRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Endpoint: /auth/register
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        // Retorna 201 Created para una creación exitosa
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint: /auth/login
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String jwt = authService.authenticate(request);
        return ResponseEntity.ok(new TokenResponse(jwt));
    }
}