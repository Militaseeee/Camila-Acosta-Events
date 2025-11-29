package com.events_cav.events_venues;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// Anotaciones estándar de integración
@SpringBootTest
@Testcontainers // Habilita la integración con Testcontainers
@ActiveProfiles("test") // Usa tu perfil de pruebas
public abstract class AbstractIntegrationTest {

    // 1. Define el Contenedor de PostgreSQL
    // El contenedor se levantará ANTES de la primera prueba y se apagará después de la última.
    @Container
    // Usamos una imagen específica para asegurar la reproducibilidad
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    // 2. Sobrescribe las propiedades de Spring
    // Esto inyecta la URL, el username y el password generados dinámicamente por Docker
    // en el contexto de Spring, asegurando que JPA apunte al contenedor.
    @DynamicPropertySource
    static void setTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        // Es importante forzar Flyway a correr en el esquema público de Postgres
        registry.add("spring.flyway.schemas", () -> "public");
    }
}