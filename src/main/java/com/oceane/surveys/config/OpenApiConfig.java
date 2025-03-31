package com.oceane.surveys.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion des Enquêtes - Oceane Consulting")
                        .description("API pour la plateforme de gestion des enquêtes de satisfaction")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe Océane Consulting")
                                .email("contact@oceane-consulting.fr"))
                        .license(new License()
                                .name("Propriétaire")
                                .url("https://www.oceaneconsulting.com/")));
    }
}
