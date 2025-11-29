package com.events_cav.events_venues.application.usecase.user;

import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.model.Role;
import com.events_cav.events_venues.domain.model.UserModel;
import com.events_cav.events_venues.domain.model.user.UserRegisterCommand;
import com.events_cav.events_venues.domain.ports.output.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserRegisterCommand createTestCommand() {
        return new UserRegisterCommand("newuser@test.com", "securepass123", Role.USER);
    }

    @Test
    void registerUser_Success() {
        // ARRANGE
        UserRegisterCommand command = createTestCommand();
        String encodedPassword = "encoded_securepass123";

        // 1. Simular: el usuario NO existe
        // CORRECCIÓN: Usar command.username()
        when(userRepositoryPort.existsByUsername(command.username())).thenReturn(false);

        // 2. Simular: el cifrado de la contraseña
        // CORRECCIÓN: Usar command.password()
        when(passwordEncoder.encode(command.password())).thenReturn(encodedPassword);

        // 3. Simular: el guardado
        when(userRepositoryPort.save(any(UserModel.class))).thenAnswer(invocation -> {
            UserModel user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // ACT
        authService.register(command);

        // ASSERT
        // 1. Verificar que se consultó la existencia
        // CORRECCIÓN: Usar command.username()
        verify(userRepositoryPort, times(1)).existsByUsername(command.username());

        // 2. Verificar que se usó el cifrador
        // CORRECCIÓN: Usar command.password()
        verify(passwordEncoder, times(1)).encode(command.password());

        // 3. Verificar que se guardó el modelo con la contraseña CIFRADA
        verify(userRepositoryPort, times(1)).save(argThat(userModel ->
                userModel.getPassword().equals(encodedPassword) &&
                        userModel.getRole().equals(command.role()) // Usamos command.role()
        ));
    }

    @Test
    void registerUser_UsernameAlreadyExists_ThrowsConflictException() {
        // ARRANGE
        UserRegisterCommand command = createTestCommand();

        // Simular: el usuario SÍ existe
        // CORRECCIÓN: Usar command.username()
        when(userRepositoryPort.existsByUsername(command.username())).thenReturn(true);

        // ACT & ASSERT
        assertThrows(ResourceConflictException.class, () -> {
            authService.register(command);
        });

        // Verificar que el guardado y el cifrador NUNCA fueron llamados
        verify(userRepositoryPort, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }
}