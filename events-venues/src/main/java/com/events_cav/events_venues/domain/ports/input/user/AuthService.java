package com.events_cav.events_venues.domain.ports.input.user;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.LoginRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.RegisterRequest;

// Usamos las DTOs de la infraestructura en el puerto, ya que son datos de entrada
// específicos del adaptador web, aunque en un diseño más estricto usarías modelos de Dominio puros.
public interface AuthService {
    /** Crea un nuevo usuario y cifra la contraseña. */
    void register(RegisterRequest request);

    /** Autentica un usuario y genera un JWT. */
    String authenticate(LoginRequest request);
}