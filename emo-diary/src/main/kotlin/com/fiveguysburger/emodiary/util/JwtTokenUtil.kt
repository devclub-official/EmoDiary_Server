package com.fiveguysburger.emodiary.util

import com.fiveguysburger.emodiary.core.service.RedisTokenService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenUtil(
    private val redisTokenService: RedisTokenService,
) {
    private val logger = LoggerFactory.getLogger(JwtTokenUtil::class.java)

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.expiration}")
    var jwtExpirationMs: Long = 0 // private에서 public으로 변경하여 외부에서 접근 가능하게 함

    @Value("\${jwt.refresh-expiration}")
    var jwtRefreshExpirationMs: Long = 0 // 리프레시 토큰 만료 시간 설정

    fun generateTokenPair(
        userId: String,
        email: String,
    ): Pair<String, String> {
        val now = Date()
        val issuedAt = now.time
        val accessTokenExpiryDate = Date(issuedAt + jwtExpirationMs)
        val refreshTokenExpiryDate = Date(issuedAt + jwtRefreshExpirationMs)
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        // Access Token 생성
        val accessToken =
            Jwts
                .builder()
                .subject(userId)
                .claim("email", email)
                .claim("tokenType", "access")
                .issuedAt(now)
                .expiration(accessTokenExpiryDate)
                .signWith(key)
                .compact()

        // Refresh Token 생성
        val refreshToken =
            Jwts
                .builder()
                .subject(userId)
                .claim("tokenType", "refresh")
                .issuedAt(now)
                .expiration(refreshTokenExpiryDate)
                .signWith(key)
                .compact()

        // Redis에 토큰 정보 저장 (새로운 형식)
        redisTokenService.storeUserTokens(
            userId = userId,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresAt = accessTokenExpiryDate.time,
            refreshTokenExpiresAt = refreshTokenExpiryDate.time,
            issuedAt = issuedAt,
        )

        return Pair(accessToken, refreshToken)
    }

    fun generateToken(
        userId: String,
        email: String,
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        val token =
            Jwts
                .builder()
                .subject(userId)
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact()

        // Redis에 JWT 토큰 저장
        redisTokenService.storeJwtToken(userId, token, jwtExpirationMs)

        return token
    }

    fun getClaims(token: String): io.jsonwebtoken.Claims? =
        try {
            val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
            Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            null
        }

    // 토큰 유효성 검증 메서드 수정
    fun validateToken(token: String): Boolean {
        try {
            val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
            val claims =
                Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .payload

            val userId = claims.subject
            val tokenType = claims.get("tokenType", String::class.java) ?: "access"

            // 토큰 타입에 따라 다른 검증 방법 사용
            return if (tokenType == "access") {
                redisTokenService.isAccessTokenValid(userId, token)
            } else {
                redisTokenService.isRefreshTokenValid(userId, token)
            }
        } catch (e: Exception) {
            logger.error("Token validation error: ${e.message}")
            return false
        }
    }

    // 로그아웃 시 Redis에서 JWT 토큰 삭제
    fun invalidateToken(userId: String): Boolean = redisTokenService.removeJwtToken(userId)
}
