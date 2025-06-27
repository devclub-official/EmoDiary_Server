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
    @JsonProperty("user_info")
    val userInfo: UserInfo? = null,
) {
    companion object {
        fun of(
            accessToken: String,
            refreshToken: String,
            expiresIn: Long,
            userInfo: UserInfo? = null,
        ): TokenResponse =
            TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = expiresIn,
                userInfo = userInfo,
            )
    }
}

data class UserInfo(
    @JsonProperty("user_id")
    val userId: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("given_name")
    val givenName: String?,
    @JsonProperty("family_name")
    val familyName: String?,
    @JsonProperty("picture")
    val picture: String?,
    @JsonProperty("locale")
    val locale: String?,
    @JsonProperty("provider")
    val provider: String,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime? = null,
    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime? = null,
) {
    companion object {
        fun fromGoogleUserInfo(
            userId: String,
            email: String,
            name: String?,
            givenName: String?,
            familyName: String?,
            picture: String?,
            locale: String?,
        ): UserInfo =
            UserInfo(
                userId = userId,
                email = email,
                name = name,
                givenName = givenName,
                familyName = familyName,
                picture = picture,
                locale = locale,
                provider = "google",
            )
    }
}
