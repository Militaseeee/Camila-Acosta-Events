package com.events_cav.events_venues.application.adapters.input.web.controller;

import com.events_cav.events_venues.AbstractIntegrationTest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.EventRequest;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Constantes para la paginación y filtros
    private final LocalDate TEST_DATE = LocalDate.of(2026, 6, 15);
    private final LocalDate TEST_DATE_END = LocalDate.of(2026, 6, 16);

    // 1. PRUEBAS DE SEGURIDAD (POST /events)
    @Test
    // Rol ADMIN: Se permite crear un Evento
    @WithMockUser(roles = {"ADMIN"})
    void createEvent_WithAdminRole_ShouldReturnCreated() throws Exception {
        EventRequest request = new EventRequest(
                null,
                "Test Concert",
                TEST_DATE,
                TEST_DATE_END,
                1L // Asumiendo que el ID 1 existe
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Espera 201 Created (si el Venue 1 existe y la validación pasa)
                .andExpect(status().isCreated());
    }

    @Test
    // Rol USER: Se deniega el acceso a crear Evento
    @WithMockUser(roles = {"USER"})
    void createEvent_WithUserRole_ShouldReturnForbidden() throws Exception {
        EventRequest request = new EventRequest(
                null,
                "Denied Event",
                TEST_DATE,
                TEST_DATE_END,
                1L
        );

        // Espera 403 Forbidden
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    // 2. PRUEBAS DE PAGINACIÓN Y FILTROS (GET /events)
    @Test
        // GET /events sin autenticar (Debe permitir el acceso)
    void getAllEvents_WithoutAuthentication_ShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
        // GET /events con filtro de ciudad
    void getAllEvents_WithCityFilter_ShouldReturnOk() throws Exception {
        // NOTA: Para que el filtro devuelva contenido, el Venue asociado debe tener la ciudad "Miami"
        mockMvc.perform(MockMvcRequestBuilders.get("/events")
                        .param("city", "Miami")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
        // Podríamos añadir .andExpect(jsonPath("$.content", hasSize(greaterThan(0)))) si tuviéramos datos precargados
    }

    // 3. PRUEBAS DE VALIDACIÓN Y ERRORES (POST /events)
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createEvent_VenueNotFound_ShouldReturnNotFound() throws Exception {
        EventRequest request = new EventRequest(
                null,
                "Bad Event",
                TEST_DATE,
                TEST_DATE_END,
                9999L // Usar un ID de Venue muy grande que no existe
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createEvent_InvalidDateRange_ShouldReturnBadRequest() throws Exception {
        // StartDate posterior a EndDate (falla la validación @ValidDateRange)
        EventRequest request = new EventRequest(
                null,
                "Invalid Date Event",
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2025, 12, 1), // Fecha incorrecta
                1L
        );

        // Espera 400 Bad Request
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}