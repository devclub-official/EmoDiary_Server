package com.fiveguysburger.emodiary.core.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("EmoDiary API")
                    .description("감정 일기 API Swagger")
                    .version("v1.0.0"),
            )
    }
}
