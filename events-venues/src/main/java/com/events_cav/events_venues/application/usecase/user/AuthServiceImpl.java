package com.events_cav.events_venues.application.usecase.user;

import com.events_cav.events_venues.domain.model.UserModel;
import com.events_cav.events_venues.domain.model.user.UserLoginCommand; // Importa el comando
import com.events_cav.events_venues.domain.model.user.UserRegisterCommand;
import com.events_cav.events_venues.domain.ports.input.user.AuthService;
import com.events_cav.events_venues.domain.ports.output.UserRepositoryPort;
import com.events_cav.events_venues.infrastructure.config.JwtService;
import lombok.RequiredArgsConstructor; // Usamos Lombok para simplificar el constructor
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                // Usa el UsernamePasswordAuthenticationToken para contener las credenciales
                new UsernamePasswordAuthenticationToken(
                        command.username(),
                        command.password()
                )
        );

        // Si la autenticación es exitosa, se obtiene el UserModel (UserDetails)
        UserModel userModel = (UserModel) authentication.getPrincipal();

        // Generar el token JWT
        // Usa el servicio JWT para crear el token basado en los detalles del usuario autenticado
        return jwtService.generateToken(userModel);
    }
}