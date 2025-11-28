package com.events_cav.events_venues.infrastructure.adapters.input.web.controller;

import com.events_cav.events_venues.domain.ports.input.user.AuthService;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.LoginRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.RegisterRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.TokenResponse;
import com.events_cav.events_venues.infrastructure.adapters.input.web.mapper.AuthRequestMapper;
import com.events_cav.events_venues.domain.model.user.UserRegisterCommand;
import com.events_cav.events_venues.domain.model.user.UserLoginCommand;
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
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

        // Mapear DTO de Infraestructura a Command de Dominio
        UserRegisterCommand command = authRequestMapper.toUserRegisterCommand(request);

        // Llamar al Puerto de Dominio con el Command
        authService.register(command);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint: /auth/login
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        // Mapear DTO de Infraestructura a Command de Dominio
        UserLoginCommand command = authRequestMapper.toUserLoginCommand(request);

        // Llamar al Puerto de Dominio con el Command
        String jwt = authService.authenticate(command);

        return ResponseEntity.ok(new TokenResponse(jwt));
    }
}