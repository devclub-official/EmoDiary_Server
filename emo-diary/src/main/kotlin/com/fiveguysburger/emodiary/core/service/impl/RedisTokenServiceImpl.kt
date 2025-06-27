package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.service.RedisTokenService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisTokenServiceImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : RedisTokenService {
    private val tokenPrefix = "jwt:token:" // 기존 메서드용
    private val userTokenPrefix = "user:" // 새로운 메서드용

    // 기존 메서드 구현 유지
    override fun storeJwtToken(
        userId: String,
        jwtToken: String,
        expirationTimeMs: Long,
    ) {
        val key = "$tokenPrefix$userId"
        redisTemplate.opsForValue().set(key, jwtToken)
        redisTemplate.expire(key, expirationTimeMs, TimeUnit.MILLISECONDS)
    }

    override fun retrieveJwtToken(userId: String): String? {
        val key = "$tokenPrefix$userId"
        return redisTemplate.opsForValue().get(key) as? String
    }

    override fun removeJwtToken(userId: String): Boolean {
        val key = "$tokenPrefix$userId"
        return redisTemplate.delete(key) ?: false
    }

    override fun isJwtTokenValid(
        userId: String,
        jwtToken: String,
    ): Boolean {
        val storedToken = retrieveJwtToken(userId)
        return storedToken != null && storedToken == jwtToken
    }

    // 새로운 메서드 구현
    override fun storeUserTokens(
        userId: String,
        accessToken: String,
        refreshToken: String,
        accessTokenExpiresAt: Long,
        refreshTokenExpiresAt: Long,
        issuedAt: Long,
    ) {
        val key = "$userTokenPrefix$userId:tokens"
        val hashValues =
            mapOf(
                "accessToken" to accessToken,
                "refreshToken" to refreshToken,
                "accessTokenExpiresAt" to accessTokenExpiresAt.toString(),
                "refreshTokenExpiresAt" to refreshTokenExpiresAt.toString(),
                "issuedAt" to issuedAt.toString(),
            )

        redisTemplate.opsForHash<String, String>().putAll(key, hashValues)

        // 리프레시 토큰 만료 시간으로 키 만료 설정 (리프레시 토큰이 더 오래 유지됨)
        val ttl = refreshTokenExpiresAt - System.currentTimeMillis()
        if (ttl > 0) {
            redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS)
        }
    }

    override fun getUserTokens(userId: String): Map<String, String>? {
        val key = "$userTokenPrefix$userId:tokens"
        val entries = redisTemplate.opsForHash<String, String>().entries(key)
        return if (entries.isEmpty()) null else entries
    }

    override fun removeUserTokens(userId: String): Boolean {
        val key = "$userTokenPrefix$userId:tokens"
        return redisTemplate.delete(key) ?: false
    }

    override fun isAccessTokenValid(
        userId: String,
        accessToken: String,
    ): Boolean {
        val tokens = getUserTokens(userId) ?: return false
        val storedToken = tokens["accessToken"] ?: return false
        val expiresAt = tokens["accessTokenExpiresAt"]?.toLongOrNull() ?: return false

        return storedToken == accessToken && expiresAt > System.currentTimeMillis()
    }

    override fun isRefreshTokenValid(
        userId: String,
        refreshToken: String,
    ): Boolean {
        val tokens = getUserTokens(userId) ?: return false
        val storedToken = tokens["refreshToken"] ?: return false
        val expiresAt = tokens["refreshTokenExpiresAt"]?.toLongOrNull() ?: return false

        return storedToken == refreshToken && expiresAt > System.currentTimeMillis()
    }
}
