package com.events_cav.events_venues.domain.model.user;

// Representa la data mínima necesaria para el proceso de autenticación (Login)
// Se usa en el Puerto de Dominio (AuthService)
public record UserLoginCommand(
        String username,
        String password
) {}