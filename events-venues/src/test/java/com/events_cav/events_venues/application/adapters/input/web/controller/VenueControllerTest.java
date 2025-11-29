package com.events_cav.events_venues.application.adapters.input.web.controller;

import com.events_cav.events_venues.AbstractIntegrationTest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.VenueRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Inicia el contexto completo de Spring Boot
@SpringBootTest
// Configura MockMvc para simular peticiones HTTP
@AutoConfigureMockMvc
// Usa el perfil 'test' (que usará H2 si aún no configuras Testcontainers)
@ActiveProfiles("test")
class VenueControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================
    // CASOS DE PRUEBA DE INTEGRACIÓN Y SEGURIDAD (POST /venues)
    // =========================================================

    @Test
    // Simula un usuario logueado con el rol ADMIN (Debe tener éxito)
    @WithMockUser(roles = {"ADMIN"})
    void createVenue_WithAdminRole_ShouldReturnCreated() throws Exception {
        // ARRANGE: Crear el DTO de solicitud
        VenueRequest request = new VenueRequest("New Admin Arena", "Test City", "123 Main St", 5000);

        // ACT & ASSERT: Simular la petición POST y esperar 201 (CREATED)
        mockMvc.perform(MockMvcRequestBuilders.post("/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    // Simula un usuario logueado con el rol USER (Debe ser denegado por @PreAuthorize)
    @WithMockUser(roles = {"USER"})
    void createVenue_WithUserRole_ShouldReturnForbidden() throws Exception {
        // ARRANGE
        VenueRequest request = new VenueRequest("Forbidden Venue", "Test City", "123 Main St", 5000);

        // ACT & ASSERT: Simular la petición POST y esperar 403 (FORBIDDEN)
        mockMvc.perform(MockMvcRequestBuilders.post("/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createVenue_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        // ARRANGE
        VenueRequest request = new VenueRequest("Unauthorized Venue", "Test City", "123 Main St", 5000);

        // ACT & ASSERT: Simular la petición POST sin JWT y esperar 401 (UNAUTHORIZED)
        mockMvc.perform(MockMvcRequestBuilders.post("/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createVenue_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // ARRANGE: Name nulo (falla la validación JSR-303)
        VenueRequest request = new VenueRequest(null, "Test City", "123 Main St", 5000);

        // ACT & ASSERT: Simular la petición POST y esperar 400 (BAD REQUEST)
        mockMvc.perform(MockMvcRequestBuilders.post("/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}