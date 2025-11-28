// Archivo: src/main/java/com/events_cav/events_venues/infrastructure/config/ApplicationConfig.java
package com.events_cav.events_venues.infrastructure.config;

// ➡️ Importar la Interfaz de Puerto de Dominio (Output Port)
import com.events_cav.events_venues.domain.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepositoryPort userRepositoryPort;

    // Define cómo obtener los detalles del usuario
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Uso el metodo del Puerto de Dominio
            // El puerto retorna un Optional<UserModel>
            return userRepositoryPort.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        };
    }

    // Define el proveedor de autenticación
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Define cómo gestionar la autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Define el cifrador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}