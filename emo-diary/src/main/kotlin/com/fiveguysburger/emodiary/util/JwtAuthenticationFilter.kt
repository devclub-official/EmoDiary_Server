package com.fiveguysburger.emodiary.util

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenUtil,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            // 1. "Authorization" 헤더에서 Bearer 토큰을 가져옴
            val bearerToken = request.getHeader("Authorization")

            // 2. Bearer 토큰이 있고 "Bearer "로 시작하는지 확인
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                val token = bearerToken.substring(7) // "Bearer " 이후의 토큰 값만 추출

                // 3. 토큰 유효성 검증
                if (jwtTokenProvider.validateToken(token)) {
                    val claims = jwtTokenProvider.getClaims(token)
                    if (claims != null) {
                        val userId = claims.subject
                        val email = claims["email"] as? String ?: ""
                        val name = claims["name"] as? String ?: ""

                        // 4. 인증 정보 생성
                        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                        val authentication = UsernamePasswordAuthenticationToken(userId, null, authorities)
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                        // 5. SecurityContext에 인증 정보 설정
                        SecurityContextHolder.getContext().authentication = authentication

                        logger.debug("Authentication set for user: $userId")
                    }
                } else {
                    logger.debug("Invalid JWT token")
                }
            } else {
                logger.debug("JWT token does not begin with Bearer String")
            }
        } catch (ex: Exception) {
            logger.error("Cannot set user authentication: ${ex.message}")
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}
