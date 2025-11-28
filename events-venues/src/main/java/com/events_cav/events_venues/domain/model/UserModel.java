// src/main/java/com/events_cav/events_venues/domain/model/UserModel.java
package com.events_cav.events_venues.domain.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Modelo de Dominio para el Usuario.
 * Implementa UserDetails para ser compatible con Spring Security.
 * No contiene anotaciones de frameworks (JPA, Spring, Lombok).
 */
public class UserModel implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Role role;

    public UserModel(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserModel() {
    }

    // Métodos de la Interfaz UserDetails (Implementación obligatoria)

    // Retorna la colección de permisos/roles del usuario.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Formato: "ROLE_ADMIN" o "ROLE_USER"
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    // Retorna la contraseña (Spring Security la usa para comparar)
    @Override
    public String getPassword() {
        return this.password;
    }

    // Retorna el nombre de usuario (Spring Security lo usa para identificar)
    @Override
    public String getUsername() {
        return this.username;
    }

    // Los cuatro métodos booleanos de estado de la cuenta (Implementaciones por defecto)
    @Override
    public boolean isAccountNonExpired() {
        return true; // La cuenta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // La cuenta no está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales (contraseña) nunca expiran
    }

    @Override
    public boolean isEnabled() {
        return true; // La cuenta está habilitada
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}