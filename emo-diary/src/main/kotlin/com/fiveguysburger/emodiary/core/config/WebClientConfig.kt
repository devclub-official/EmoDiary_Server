package com.fiveguysburger.emodiary.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient =
        WebClient
            .builder()
            .baseUrl("http://emodiary.duckdns.org:8080") // 필요한 기본 URL 설정
            .build()
}
