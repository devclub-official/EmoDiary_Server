package com.fiveguysburger.emodiary.core.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class TokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("token_type")
    val tokenType: String = "Bearer",
    @JsonProperty("expires_in")
    val expiresIn: Long,
    @JsonProperty("issued_at")
    val issuedAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun of(
            accessToken: String,
            refreshToken: String,
            expiresIn: Long,
        ): TokenResponse =
            TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = expiresIn,
            )
    }
}
