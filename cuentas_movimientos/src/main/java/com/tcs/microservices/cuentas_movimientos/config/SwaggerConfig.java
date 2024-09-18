package com.tcs.microservices.cuentas_movimientos.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Cuentas y Movimientos")
                        .version("V1")
                        .description("Documentación de la API para la gestión de cuentas y movimientos"));
    }

    @Bean
    public GroupedOpenApi cuentasApi() {
        return GroupedOpenApi.builder()
                .group("cuentas-movimientos")
                .pathsToMatch("/api/v1/cuentas/**", "/api/v1/movimientos/**", "/api/v1/reportes/**")
                .build();
    }
}
