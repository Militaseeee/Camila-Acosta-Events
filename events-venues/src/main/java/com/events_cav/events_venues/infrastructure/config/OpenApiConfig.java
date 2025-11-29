package com.events_cav.events_venues.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement; // <-- Nueva Importación
import io.swagger.v3.oas.models.security.SecurityScheme; // <-- Nueva Importación
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth"; // Nombre de la configuración de seguridad

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                // Añadir la información general
                .info(new Info()
                        .title("Events & Venues API")
                        .contact(new Contact()
                                .name("Camila Acosta")
                                .email("camilitaacosta2001@gmail.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                )
                // Definir el esquema de seguridad (Bearer Token)
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                )
                // Aplicar el esquema de seguridad globalmente
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}