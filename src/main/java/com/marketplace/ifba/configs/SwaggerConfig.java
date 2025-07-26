package com.marketplace.ifba.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Marketplace IFBA API")
                .version("1.0")
                .description("API desenvolvida para a aplicação do Markeplace IFBA"));
    }
}
