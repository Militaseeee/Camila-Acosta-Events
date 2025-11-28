// src/main/java/com/events_cav/events_venues/application/usecase/AuthServiceImpl.java
package com.events_cav.events_venues.application.usecase.user;

import com.events_cav.events_venues.domain.model.Role;
import com.events_cav.events_venues.domain.model.UserModel;
import com.events_cav.events_venues.domain.ports.output.UserRepositoryPort;
import com.events_cav.events_venues.infrastructure.config.JwtService;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.LoginRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {
        // 1. Cifrar la contraseña
        String encodedPassword = passwordEncoder.encode(request.password());

        // 2. Crear el modelo de dominio
        UserModel user = UserModel.builder()
                .username(request.username())
                .password(encodedPassword)
                // Asignar rol por defecto, asumo USER si no se especifica
                .role(Role.valueOf(request.role().toUpperCase()))
                .build();

        // 3. Guardar el usuario (a través del puerto)
        userRepositoryPort.save(user);
    }

    @Override
    public String authenticate(LoginRequest request) {
        // 1. Autenticar usando el AuthenticationManager de Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // 2. Si la autenticación fue exitosa, obtener el UserDetails
        UserModel user = userRepositoryPort.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.username()));

        // 3. Generar el JWT
        return jwtService.generateToken(user);
    }
}