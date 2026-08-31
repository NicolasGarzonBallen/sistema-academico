package com.sistema.academico.sistema_academico.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sistemaAcademicoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Académico — API REST")
                        .description("Laboratorio 2 — Sistemas Distribuidos. CRUD de Estudiantes, Materias e Inscripciones.")
                        .version("v1"));
    }
}
