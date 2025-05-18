package com.fiveguysburger.emodiary.core.login.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fiveguysburger.emodiary.core.login.service.LoginService
import com.fiveguysburger.emodiary.util.JwtTokenUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class LoginServiceImpl(
    private val webClient: WebClient,
    private val jwtTokenProvider: JwtTokenUtil,
    private val objectMapper: ObjectMapper,
) : LoginService {
    // Google OAuth2 설정값 추가
    @Value("\${spring.security.oauth2.client.registration.google.client-id}")
    private lateinit var googleClientId: String

    @Value("\${spring.security.oauth2.client.registration.google.client-secret}")
    private lateinit var googleClientSecret: String

    @Value("\${spring.security.oauth2.client.registration.google.redirect-uri}")
    private lateinit var googleRedirectUri: String

    // Google OAuth2 엔드포인트
    @Value("\${spring.security.oauth2.client.provider.google.token-uri}")
    private lateinit var googleTokenUri: String

    @Value("\${spring.security.oauth2.client.provider.google.user-info-uri}")
    private lateinit var googleUserInfoUri: String

    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}")
    private lateinit var kakaoClientId: String

    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret}")
    private lateinit var kakaoClientSecret: String

    @Value("\${spring.security.oauth2.client.provider.kakao.token-uri}")
    private lateinit var kakaoTokenUri: String

    @Value("\${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private lateinit var kakaoUserInfoUri: String

    override fun loginWithGoogle(code: String) {
        TODO("Not yet implemented")
    }

    override fun loginWithKakao1(code: String) {
        TODO("Not yet implemented")
    }
}
