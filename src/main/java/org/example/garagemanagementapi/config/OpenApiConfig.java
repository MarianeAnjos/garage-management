package org.example.garagemanagementapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Estapar - API de Gestão de Estacionamento")
                .version("1.0")
                .description("API para controle de entrada, saída e faturamento de vagas"));
    }
}
