package edu.uit.se122.server.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bmsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Badminton Management System API")
                        .description("Tài liệu API cho hệ thống quản lý sân cầu lông")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
