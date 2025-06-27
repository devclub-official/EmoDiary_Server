package com.fiveguysburger.emodiary.core.service

interface RedisTokenService {
    // 기존 메서드는 유지하되 사용하지 않을 예정
    fun storeJwtToken(
        userId: String,
        jwtToken: String,
        expirationTimeMs: Long,
    )

    fun retrieveJwtToken(userId: String): String?

    fun removeJwtToken(userId: String): Boolean

    fun isJwtTokenValid(
        userId: String,
        jwtToken: String,
    ): Boolean

    // 새로운 메서드 추가
    fun storeUserTokens(
        userId: String,
        accessToken: String,
        refreshToken: String,
        accessTokenExpiresAt: Long,
        refreshTokenExpiresAt: Long,
        issuedAt: Long,
    )

    fun getUserTokens(userId: String): Map<String, String>?

    fun removeUserTokens(userId: String): Boolean

    fun isAccessTokenValid(
        userId: String,
        accessToken: String,
    ): Boolean

    fun isRefreshTokenValid(
        userId: String,
        refreshToken: String,
    ): Boolean
}
