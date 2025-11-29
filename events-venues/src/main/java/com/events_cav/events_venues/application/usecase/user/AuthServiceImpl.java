package com.events_cav.events_venues.application.usecase.user;

import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.model.UserModel;
import com.events_cav.events_venues.domain.model.user.UserLoginCommand;
import com.events_cav.events_venues.domain.model.user.UserRegisterCommand;
import com.events_cav.events_venues.domain.ports.input.user.AuthService;
import com.events_cav.events_venues.domain.ports.output.UserRepositoryPort;
import com.events_cav.events_venues.infrastructure.config.JwtService;
import lombok.RequiredArgsConstructor; // Usamos Lombok para simplificar el constructor
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.events_cav.events_venues.infrastructure.config.CustomUserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Genera un constructor con todos los campos finales (simplifica la DI)
public class AuthServiceImpl implements AuthService {

    // Necesitas estos servicios para la lógica de autenticación
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryPort userRepositoryPort;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public void register(UserRegisterCommand command) {

        if (userRepositoryPort.existsByUsername(command.username())) {
            throw new ResourceConflictException("User with username '" + command.username() + "' already exists");
        }

        UserModel newUser = new UserModel();
        newUser.setUsername(command.username());
        newUser.setRole(command.role());

        String hashedPassword = passwordEncoder.encode(command.password());
        newUser.setPassword(hashedPassword);

        userRepositoryPort.save(newUser);
    }

    @Override
    public String authenticate(UserLoginCommand command) {
        // Intentar autenticar el usuario usando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.username(),
                        command.password()
                )
        );

        // Obtener el CustomUserDetails que Spring Security devolvió
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 2. Extraer el UserModel puro del Dominio, si se necesita para lógica de negocio
        UserModel userModel = userDetails.getUserModel();
        // ^^^ Asumiendo que agregaste un metodo getUserModel() al CustomUserDetails.

        // Generar el token JWT
        // Debes pasar el userDetails a jwtService.generateToken para que use los métodos de Spring Security
        return jwtService.generateToken(userDetails);
    }
}