package com.events_cav.events_venues.infrastructure.config;

import com.events_cav.events_venues.domain.model.UserModel; // <-- Importa el Modelo de Dominio
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 * Adaptador: Implementa UserDetails (Infraestructura) y envuelve el UserModel (Dominio).
 * Esto mantiene la capa de Dominio limpia.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    // 1. Almacena una referencia al modelo de Dominio
    private final UserModel userModel;

    // Métodos de UserDetails que DELEGAN al UserModel

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Usa el Role del Dominio para crear la autoridad de Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + userModel.getRole().name()));
    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

    // Métodos de estado de la cuenta (se asume true, como antes)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserModel getUserModel() {
        return userModel;
    }
}