package com.events_cav.events_venues.domain.model.user;

import com.events_cav.events_venues.domain.model.Role;

// Usar un record es ideal para comandos inmutables
public record UserRegisterCommand(
        String username,
        String password, // Contraseña en texto plano temporalmente
        Role role
) {}