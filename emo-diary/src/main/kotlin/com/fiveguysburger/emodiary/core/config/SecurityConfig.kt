package com.fiveguysburger.emodiary.core.config

import com.fiveguysburger.emodiary.util.JwtAuthenticationFilter
import com.fiveguysburger.emodiary.util.JwtTokenUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenUtil: JwtTokenUtil,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.authorizeHttpRequests { auth ->
                auth
                    // Swagger UI 관련 경로 허용
                    .requestMatchers("/swagger-ui/**")
                    .permitAll()
                    .requestMatchers("/swagger-ui.html")
                    .permitAll()
                    .requestMatchers("/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers("/swagger-resources/**")
                    .permitAll()
                    .requestMatchers("/webjars/**")
                    .permitAll()
                    .requestMatchers("/api/login/**")
                    .permitAll()
                    .requestMatchers("/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.addFilterBefore(JwtAuthenticationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter::class.java)
            .oauth2Login { oauth2 ->
                oauth2
                    .defaultSuccessUrl("/loginSuccess")
                    .failureUrl("/loginFailure")
            }

        return http.build()
    }
}
